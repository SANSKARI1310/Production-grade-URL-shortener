package production_grade_url_shortener.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import production_grade_url_shortener.controller.RateLimitInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    public WebConfig(RateLimitInterceptor rateLimitInterceptor)
    {
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/url").addPathPatterns("/api/url/*");
    }
}
