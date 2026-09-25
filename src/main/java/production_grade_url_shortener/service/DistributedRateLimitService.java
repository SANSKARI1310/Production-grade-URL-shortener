package production_grade_url_shortener.service;

import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import production_grade_url_shortener.dto.RateLimitResult;

@Service 
public class DistributedRateLimitService {
    
    private static final Logger log = LoggerFactory.getLogger(DistributedRateLimitService.class);
    private final StringRedisTemplate stringRedisTemplate; // to consume from redis and help 
    private final RedisScript<List> redisScript;
    private final RateLimitingService fallBackRateLimitingService;
    public DistributedRateLimitService(StringRedisTemplate stringRedisTemplate , RedisScript<List> redisScript , RateLimitingService fallBackRateLimitingService)
    {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisScript = redisScript;
        this.fallBackRateLimitingService = fallBackRateLimitingService;
    }
    public RateLimitResult tryConsume(String key, double capacity, double refillTokensPerSecond, double cost, boolean isMutation)
    {
        try
        {
            long now = System.currentTimeMillis();
            double refillPerMs = refillTokensPerSecond / 1000.0;
            long ttl = Math.max(60l , (long)(capacity / refillTokensPerSecond) * 2);
            List<?> rawResult = stringRedisTemplate.execute(
                redisScript,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillPerMs),
                String.valueOf(cost),
                String.valueOf(now),
                String.valueOf(ttl),
                String.valueOf(isMutation)
            );

            if(rawResult !=null && rawResult.size()>=2)
            {
                long allowedFlag = ((Number)rawResult.get(0)).longValue();
                long remainingTokens = ((Number)rawResult.get(1)).longValue();
                return new RateLimitResult(allowedFlag == 1L, remainingTokens , false);
            }
            return new RateLimitResult(true , 0 , false);
        }
        catch(Exception e)
        {
           // log.warn("Redis rate limiter unavailable for key '{}' , Error : {}.. Starting fallback" , key , e.getMessage() );
           log.warn("redis unavailable for key '{}' " , key , e);
            if(!isMutation)
            {
                log.info("failing open for read Key: {}" , key);
                return new RateLimitResult(true , -1, true);
            }

            log.info("Applying fallback for key: {}" , key);
            boolean allowed = fallBackRateLimitingService.resolveBucket(key).tryConsume((long)cost);
            long remainingTokens = fallBackRateLimitingService.resolveBucket(key).getAvailableTokens();
            return new RateLimitResult(allowed , remainingTokens , true );
         }
    }

}
