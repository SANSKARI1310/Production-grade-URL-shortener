package production_grade_url_shortener.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import production_grade_url_shortener.event.UrlClickEvent;
import production_grade_url_shortener.service.AnalyticsService;


@Component
public class AnalyticsConsumer {
    private final static Logger log = LoggerFactory.getLogger(AnalyticsConsumer.class);
    private final AnalyticsService analyticsService;
    public AnalyticsConsumer(AnalyticsService analyticsService)
    {
        this.analyticsService = analyticsService;
    }

    @KafkaListener(topics = "url-clicks" )
    public void consume(UrlClickEvent event)
    {
        try
        {
            analyticsService.handleUrlClick(event);
            log.debug("Persisted click event from kafka for the code {}" , event.shortcode());
        }
        catch(Exception e)
        {
            log.error("Error while persisting click event for the code {}" ,event.shortcode() ,e);
        }
    }

}
