package production_grade_url_shortener.config;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;


@Configuration 
@EnableAsync 
public class AsyncEventConfig {

    private static final Logger log = LoggerFactory.getLogger(AsyncEventConfig.class);
    private final MeterRegistry meterRegistry;
    AsyncEventConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    @Bean("analyticsExecutor")
    public Executor analyticsExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(5000);
        executor.setThreadNamePrefix("analytics-pub");

        Counter droppedCounter = meterRegistry.counter("analytics.dropped");

        executor.setRejectedExecutionHandler((r, exec) -> {
            droppedCounter.increment();
            log.warn("Dropped analytics event reached");
        }
        );
        
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(5);
        executor.initialize();
        return executor;
    }
}
