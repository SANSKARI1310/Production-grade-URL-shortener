package production_grade_url_shortener.controller;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.Set;

import production_grade_url_shortener.dto.RateLimitResult;
import production_grade_url_shortener.service.DistributedRateLimitService;

@Component
public class RateLimitInterceptor  implements HandlerInterceptor {
    
    private static final Set<String> paths= Set.of("POST" , "PUT" , "DELETE");
    private final DistributedRateLimitService rateLimitingService;
    public RateLimitInterceptor(DistributedRateLimitService rateLimitingService)
    {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String uri= request.getRequestURI();
        boolean isMutation = paths.contains(request.getMethod());

        String key;
        double capacity;
        double refillRate;

        if(uri.startsWith("/r/"))
        {
            String clientIp = extractClientIp(request);
            key = "rate:ip:" + clientIp;
            capacity = 50.0;
            refillRate = 5.0;
        }
        else if (uri.startsWith("/api/"))
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String identity = (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) ? auth.getName() : extractClientIp(request);
            key = "rate:user:" + identity;
            capacity = isMutation ? 10.0 : 30.0;
            refillRate = isMutation ? 2.0 : 10.0;
        }
        else
        {
            return true;
        }
        RateLimitResult result = rateLimitingService.tryConsume(key, capacity, refillRate, 1.0, isMutation);
        if(result.allowed())
        {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(result.remainingTokens()));
            if(result.degradedFallback())
            {
                response.addHeader("X-Rate-Limit-Mode" , "Degreaded local fallback");
            }
            return true;
        }
        else
        {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                        "error" : "Too many requests"
                        "message" : "You have exceeded the rate limit"
                    }
                    """
            );
            return false;
        }
      
    }
    private String extractClientIp(HttpServletRequest request)
    {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }


}
