package production_grade_url_shortener.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import production_grade_url_shortener.dto.AnalyticsResponse;
import production_grade_url_shortener.service.AnalyticsService;

@RestController 
@RequestMapping("/api/urls")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    public AnalyticsController(AnalyticsService analyticsService)
    {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/{shortcode}/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String shortcode)
    {
        AnalyticsResponse response  = analyticsService.getAnalytics(shortcode);
        return ResponseEntity.ok(response);
    }
}
