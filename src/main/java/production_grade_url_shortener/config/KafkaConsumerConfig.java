package production_grade_url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
        KafkaTemplate<Object , Object > kafkaTemplate)
    {
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        FixedBackOff backOff =
                new FixedBackOff(2000L, 2L);

        return new DefaultErrorHandler(recoverer, backOff);
    }

    @Bean 

    public ConcurrentKafkaListenerContainerFactory<String , Object> kafkaListenerContainerFactory(
        ConsumerFactory<String , Object> consumerFactory,
        DefaultErrorHandler errorHandler
    )
    {
        ConcurrentKafkaListenerContainerFactory<String , Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
