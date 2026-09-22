package production_grade_url_shortener.filter;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import production_grade_url_shortener.repository.ApiKeyRepository;
import production_grade_url_shortener.entity.ApiKey;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    
    private ApiKeyRepository apiKeyRepository;
    public ApiKeyAuthenticationFilter(ApiKeyRepository apiKeyRepository)
    {
        this.apiKeyRepository = apiKeyRepository;
    }
    public void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException , IOException
    {
        String apiKey = request.getHeader("X-API-KEY");
        if(apiKey == null)
        {
            response.setStatus(401);
            return;
        }
        String hashedApiKey = hashApiKey(apiKey);
        Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByApiHashAndIsActiveApi(hashedApiKey);
        if(!apiKeyOptional.isPresent())
        {
            response.setStatus(401);
            return;
        }
        ApiKey validKey = apiKeyOptional.get();
        // saves the apikey and user related data for the time HTTPRequest is alive
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(validKey.getUserId(), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request , response);
    }

    private String hashApiKey(String plainApiKey)
    {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainApiKey.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash API key", e);
        }
    }
}
