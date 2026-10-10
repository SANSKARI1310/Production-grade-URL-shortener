package production_grade_url_shortener.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import production_grade_url_shortener.event.UrlClickEvent;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component 
public class AnalyticsEventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventPublisher.class);
    private static final String TOPIC = "url-click-events";

    private final KafkaTemplate<String , UrlClickEvent> kafkaTemplate;
    private final Counter successCounter;
    private final Counter failureCounter;
    public AnalyticsEventPublisher(KafkaTemplate<String , UrlClickEvent> kafkaTemplate , MeterRegistry meterRegistry)
    {
        this.kafkaTemplate = kafkaTemplate;
        this.successCounter = meterRegistry.counter("analytics.kafka.publish.success");
        this.failureCounter = meterRegistry.counter("analytics.kafka.publish.failure");
    }
    @Async ("analyticsExecutor")
    public void publishClickEvent(UrlClickEvent event)
    {
        kafkaTemplate.send(TOPIC ,event.shortcode(), event)
        .whenComplete((result , ex) -> 
            {
                if(ex==null)
                {
                    successCounter.increment();
                }
                else
                {
                    failureCounter.increment();
                    log.error("Error while publishing event for shortcode {} after internal retries {}" , event.shortcode() , ex.getMessage());
                }
            });
    }

}
