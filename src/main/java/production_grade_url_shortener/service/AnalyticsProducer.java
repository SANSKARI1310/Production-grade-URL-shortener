    package production_grade_url_shortener.service;

    import org.springframework.stereotype.Service;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.kafka.core.KafkaTemplate;
    import production_grade_url_shortener.event.UrlClickEvent;
    import io.micrometer.core.instrument.Counter;
    import io.micrometer.core.instrument.MeterRegistry;
    import org.springframework.scheduling.annotation.Async;

    @Service
    public class AnalyticsProducer {
    
        private static final Logger log = LoggerFactory.getLogger(AnalyticsProducer.class);
        private final KafkaTemplate<String , UrlClickEvent> kafkaTemplate;
        private final String TOPIC = "url-clicks";

        private Counter failureCounter;
        private Counter successCounter;
        public AnalyticsProducer(KafkaTemplate<String , UrlClickEvent> kafkaTemplate , MeterRegistry meterRegistry)
        {
            this.kafkaTemplate = kafkaTemplate;
            this.successCounter = meterRegistry.counter("analytics.kafka.publish.success");
            this.failureCounter = meterRegistry.counter("analytics.kafka.publish.failure"); 
        }
        @Async("analyticsExecutor")
        public void publishEvent(UrlClickEvent event)
        {
            kafkaTemplate.send(TOPIC ,event.shortcode(), event)
            .whenComplete((result , ex) -> {
                if(ex != null)
                {
                    failureCounter.increment();
                    log.error("Error while publishing event to kafka" , ex);
                }
                else{
                    successCounter.increment();
                    log.debug("Published event to kafka");
                }

            });

        }
    }
