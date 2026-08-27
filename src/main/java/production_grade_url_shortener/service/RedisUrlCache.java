package production_grade_url_shortener.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RedisUrlCache {
    
    private final StringRedisTemplate redisTemplate;
    private final String NOT_FOUND = "NOT_FOUND";
    public RedisUrlCache(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    public String get(String shortCode)
    {
        return redisTemplate.opsForValue().get(key(shortCode));
    }

    public void set(String shortCode , String url , Duration ttl)
    {
        redisTemplate.opsForValue().set(key(shortCode) , url , ttl);
    }

    public void setNotFound(String shortcode , Duration ttl)
    {
        redisTemplate.opsForValue().set(shortcode , NOT_FOUND, ttl);
    }
    public void evict(String shortcode)
    {
        redisTemplate.delete(key(shortcode));
    }
    private String key(String shortCode)
    {
        return "url:"+shortCode;
    }

}
