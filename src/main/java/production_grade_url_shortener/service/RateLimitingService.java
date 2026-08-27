package production_grade_url_shortener.service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitingService {
    
    private final Map<String , Bucket> buckets = new ConcurrentHashMap<>();
    private Bucket createBucket(String ipAddress)
    {
        Bandwidth limit = Bandwidth.builder()
        .capacity(10)
        .refillGreedy(2, Duration.ofSeconds(1))
        .build();

        return Bucket.builder()
        .addLimit(limit).build();
    }

    public Bucket resolveBucket(String clientIp)
    {
         return buckets.computeIfAbsent(clientIp, this::createBucket);
        
    }

}
