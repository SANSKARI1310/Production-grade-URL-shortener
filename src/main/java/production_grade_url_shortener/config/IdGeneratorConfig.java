package production_grade_url_shortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import production_grade_url_shortener.util.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import production_grade_url_shortener.util.SnowFlakeIdgenerator;

@Configuration
public class IdGeneratorConfig {
    
    @Bean
    public IdGenerator idGenerator(
        @Value("${snowflake.worker-id}") long workerId
            )
    {
        return new SnowFlakeIdgenerator(workerId);
    }
}
