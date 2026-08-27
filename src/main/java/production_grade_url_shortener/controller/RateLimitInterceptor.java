package production_grade_url_shortener.controller;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import production_grade_url_shortener.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;

@Component
public class RateLimitInterceptor  implements HandlerInterceptor {
    
    private final RateLimitingService rateLimitingService;
    public RateLimitInterceptor(RateLimitingService rateLimitingService)
    {
        this.rateLimitingService = rateLimitingService;
    }

    @Override

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (!request.getMethod().equals("POST")) {
        return true;
        }   
        String ipAddress = request.getRemoteAddr();
        Bucket tokenBucket = rateLimitingService.resolveBucket(ipAddress);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if(probe.isConsumed())
        {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        }
        else
        {
            long waitTime = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitTime));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "You have exhausted your API request limit");
            return false;
        }
      
    }


}
