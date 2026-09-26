package production_grade_url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import production_grade_url_shortener.repository.ApiKeyRepository;
import org.springframework.security.config.http.SessionCreationPolicy;
import production_grade_url_shortener.filter.ApiKeyAuthenticationFilter;
import production_grade_url_shortener.security.ApiKeyGenerator;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGenerator apiKeyGenerator;
    public SecurityConfig(ApiKeyRepository apiKeyRepository , ApiKeyGenerator apiKeyGenerator)
    {
        this.apiKeyRepository = apiKeyRepository;
        this.apiKeyGenerator = apiKeyGenerator;
    }
    
    @Bean
    public SecurityFilterChain securityfilterChain(HttpSecurity http) throws Exception
    {
        //this helps with with api and not cache
        http.csrf(csrf ->csrf.disable());
        http.cors(cors->{});
        http.sessionManagement(sessionManagement ->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize ->{
            authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            authorize.requestMatchers("/r/**").permitAll();
            authorize.requestMatchers("/api/auth/**").permitAll();
            authorize.requestMatchers("/api/urls/**").authenticated();
            authorize.requestMatchers("/api/analytics/**").authenticated();
            authorize.anyRequest().authenticated();

        });

        http.addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyRepository, apiKeyGenerator), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
