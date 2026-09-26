package production_grade_url_shortener.filter;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import production_grade_url_shortener.repository.ApiKeyRepository;
import production_grade_url_shortener.entity.ApiKey;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import production_grade_url_shortener.security.ApiKeyGenerator;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    
    private static final String key_prefix = "pgurl_live_";
    private ApiKeyGenerator apiKeyGenerator;
    private ApiKeyRepository apiKeyRepository;
    public ApiKeyAuthenticationFilter(ApiKeyRepository apiKeyRepository , ApiKeyGenerator apiKeyGenerator)
    {
        this.apiKeyRepository = apiKeyRepository;
        this.apiKeyGenerator = apiKeyGenerator;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // public routes
        return path.startsWith("/r/") || path.startsWith("/api/auth/") || path.equals("/error");
    }

    @Override 
    public void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException , IOException
    {
        String apiKey = request.getHeader("X-API-KEY");
        if(apiKey == null || !apiKey.startsWith(key_prefix))
        {
            filterChain.doFilter(request, response);
            return;
        }
        String hashedApiKey = apiKeyGenerator.hashKey(apiKey);

        Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByApiHashAndIsActiveApi(hashedApiKey,true);
        if(!apiKeyOptional.isPresent())
        {
            filterChain.doFilter(request, response);
            return;
        }
        ApiKey validKey = apiKeyOptional.get();
        // saves the apikey and user related data for the time HTTPRequest is alive
        //securitycontext value
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(validKey.getUserId(), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request , response);
    }

}
