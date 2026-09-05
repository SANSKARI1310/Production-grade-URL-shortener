    package production_grade_url_shortener.service;

    import org.springframework.stereotype.Service;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.kafka.core.KafkaTemplate;
    import production_grade_url_shortener.event.UrlClickEvent;

    @Service
    public class AnalyticsProducer {
    
        private static final Logger log = LoggerFactory.getLogger(AnalyticsProducer.class);
        private final KafkaTemplate<String , Object> kafkaTemplate;
        private final String TOPIC = "url-clicks";
        public AnalyticsProducer(KafkaTemplate<String , Object> kafkaTemplate)
        {
            this.kafkaTemplate = kafkaTemplate;
        }
        public void publishEvent(UrlClickEvent event)
        {
            kafkaTemplate.send(TOPIC ,event.shortcode(), event)
            .whenComplete((result , ex) -> {
                if(ex != null)
                    log.error("Error while publishing event to kafka" , ex);
                else
                    log.debug("Published event to kafka");

            });

        }
    }
