package production_grade_url_shortener.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import production_grade_url_shortener.dto.CreateUrlRequest;
import production_grade_url_shortener.dto.CreateUrlResponse;
import production_grade_url_shortener.entity.Url;
import production_grade_url_shortener.repository.UrlRepository;
import production_grade_url_shortener.util.Base62Encoder;
import production_grade_url_shortener.util.IdGenerator;
import production_grade_url_shortener.exceptions.ResourceNotFoundException;
import production_grade_url_shortener.exceptions.UrlNotFoundException;
import production_grade_url_shortener.util.UrlValidator;
import production_grade_url_shortener.dto.UpdateUrlRequest;
import production_grade_url_shortener.dto.UpdateUrlResponse;

@Service
public class UrlService {
    private final Base62Encoder base62Encoder;
    private final UrlRepository urlRepository;
    private final IdGenerator idGenerator;
    private final UrlValidator urlValidator;
    private final RedisUrlCache redisUrlCache;
    private final Duration ttl_default = Duration.ofHours(1);

    public UrlService(Base62Encoder base62Encoder , UrlRepository urlRepository , IdGenerator idGenerator , UrlValidator urlValidator , RedisUrlCache redisUrlCache)
    {
        this.base62Encoder = base62Encoder;
        this.urlRepository = urlRepository;
        this.idGenerator = idGenerator;
        this.urlValidator = urlValidator;
        this.redisUrlCache = redisUrlCache;
    }

    public CreateUrlResponse createUrl(CreateUrlRequest request)
    {
        long id = idGenerator.generateId();
        String shortCode = base62Encoder.encode(id);
        String normalizedUrl = urlValidator.validateAndNormalizeUrl(request.getOriginalUrl());
        Url url = new Url(id , shortCode , normalizedUrl , Instant.now() , request.getExpiresAt());
        urlRepository.save(url);
        return new CreateUrlResponse(shortCode , request.getOriginalUrl() , url.getCreatedAt(), url.getExpiresAt() );
    }

    @Transactional
    public UpdateUrlResponse updateUrl(String shortcode , UpdateUrlRequest request)
    {
        Url url = urlRepository.findByShortcode(shortcode).orElseThrow(()-> new ResourceNotFoundException("Shortcode not found"));
        url.setOriginalUrl(urlValidator.validateAndNormalizeUrl(request.getOriginalUrl()));
        urlRepository.save(url);
        redisUrlCache.evict(shortcode);
        return new UpdateUrlResponse(url.getOriginalUrl() , url.getCreatedAt() , url.getExpiresAt());
    }

    @Transactional
    public void deleteUrl(String shortcode)
    {
        Url url = urlRepository.findByShortcode(shortcode).orElseThrow(()-> new ResourceNotFoundException("Shortcode not found"));
        urlRepository.delete(url);
        redisUrlCache.evict(shortcode);;

    }
    @Transactional(readOnly = true)
    public String getDestinationUrl(String shortcode)
    {
        String cachedUrl = redisUrlCache.get(shortcode);
        if(cachedUrl != null)
           {
                if(cachedUrl.equals("NOT_FOUND"))
                {
                    throw new ResourceNotFoundException("ShortCode not found");
                }
                return cachedUrl;
           } 
        Url url = urlRepository.findByShortcode(shortcode)
        .orElseThrow(() -> {
            redisUrlCache.setNotFound(
                shortcode,
                Duration.ofSeconds(30)
            );
            return new ResourceNotFoundException("Shortcode not found");
        });
        if(!url.isActive())
        {
            throw new ResourceNotFoundException("ShortUrl is not active" +shortcode);
        }
        if(url.isExpired())
        {
            throw new UrlNotFoundException("ShortUrl has expired" +shortcode);
        }
        Duration ttl;
        if(url.getExpiresAt() != null)
        {
            ttl = Duration.between(Instant.now() , url.getExpiresAt());
        }
        else
        {
            ttl = ttl_default;
        }
        if(!ttl.isZero() && !ttl.isNegative())
        {
            redisUrlCache.set(shortcode, url.getOriginalUrl() , ttl);
        }
        return url.getOriginalUrl();
    }
}
