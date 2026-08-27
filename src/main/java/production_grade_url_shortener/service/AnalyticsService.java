package production_grade_url_shortener.service;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import production_grade_url_shortener.event.UrlClickEvent;

@Service
public class AnalyticsService {
    
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    @Async("AnalyticsExecutor")
    @EventListener
    public void handleUrlClick(UrlClickEvent event)
    {
        log.info("Background task tracked -- Code: {} , Ip: {} , TimeStamp: {}" , event.shortcode() , event.ipAddress() , event.timestamp());

    }

}
