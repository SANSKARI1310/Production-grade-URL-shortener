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
import java.util.UUID;
import production_grade_url_shortener.service.AnalyticsProducer;

@RestController
@RequestMapping("/r")
public class RedirectController {
    
    private UrlService urlService;
    private final AnalyticsProducer analyticsProducer;
    
    public RedirectController(UrlService urlService , AnalyticsProducer analyticsProducer)
    {
        this.analyticsProducer = analyticsProducer;
        this.urlService = urlService;
    }
    @GetMapping("/{shortcode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortcode , HttpServletRequest request)
    {
        String originalUrl = urlService.getDestinationUrl(shortcode);
        String eventId = UUID.randomUUID().toString();
        String ipAddress = request.getHeader("X-Forwarded-For");
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        if(ipAddress == null) ipAddress = request.getRemoteAddr();
        analyticsProducer.publishEvent(new UrlClickEvent(eventId, originalUrl, shortcode , ipAddress , userAgent, referer,Instant.now()));

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }
    
}
