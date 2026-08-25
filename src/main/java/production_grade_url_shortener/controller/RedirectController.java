package production_grade_url_shortener.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;


import production_grade_url_shortener.service.UrlService;

@RestController
@RequestMapping("/r")
public class RedirectController {
    
    private UrlService urlService;
    public RedirectController(UrlService urlService)
    {
        this.urlService = urlService;
    }
    @GetMapping("/{shortcode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortcode)
    {
        String originalUrl = urlService.getDestinationUrl(shortcode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl))
        .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate").build();
    }
    
}
