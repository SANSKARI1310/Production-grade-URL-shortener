package production_grade_url_shortener.service;

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

@Service
public class UrlService {
    Base62Encoder base62Encoder;
    UrlRepository urlRepository;
    IdGenerator idGenerator;
    UrlValidator urlValidator;

    public UrlService(Base62Encoder base62Encoder , UrlRepository urlRepository , IdGenerator idGenerator , UrlValidator urlValidator)
    {
        this.base62Encoder = base62Encoder;
        this.urlRepository = urlRepository;
        this.idGenerator = idGenerator;
        this.urlValidator = urlValidator;
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

    @Transactional(readOnly = true)
    public String getDestinationUrl(String shortcode)
    {
        Url url= urlRepository.findByShortcode(shortcode).orElseThrow(()-> new ResourceNotFoundException("Shortcode not found"));
        if(!url.isActive())
        {
            throw new ResourceNotFoundException("ShortUrl is not active" +shortcode);
        }
        if(url.isExpired())
        {
            throw new UrlNotFoundException("ShortUrl has expired" +shortcode);
        }
        return url.getOriginalUrl();
    }
}
