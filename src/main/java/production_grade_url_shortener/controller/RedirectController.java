package production_grade_url_shortener.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import production_grade_url_shortener.event.UrlClickEvent;
import production_grade_url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;

@RestController
@RequestMapping("/r")
public class RedirectController {
    
    private UrlService urlService;
    private final ApplicationEventPublisher eventPublisher;
    public RedirectController(UrlService urlService , ApplicationEventPublisher eventPublisher)
    {
        this.urlService = urlService;
        this.eventPublisher = eventPublisher;
    }
    @GetMapping("/{shortcode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortcode , HttpServletRequest request)
    {
        String originalUrl = urlService.getDestinationUrl(shortcode);
        String ipAddress = request.getHeader("X-Forwarded-For");
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        if(ipAddress == null) ipAddress = request.getRemoteAddr();
        eventPublisher.publishEvent(new UrlClickEvent(originalUrl, shortcode , ipAddress , userAgent, referer,Instant.now()));

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }
    
}
