package production_grade_url_shortener;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import production_grade_url_shortener.util.IdGenerator;
import production_grade_url_shortener.util.SnowFlakeIdgenerator;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class snowflakeConcurrency {
    
     @Test
    void shouldGenerateUniqueIdsConcurrently() throws InterruptedException {

        IdGenerator generator = new SnowFlakeIdgenerator(1);

        int threads = 20;
        int idsPerThread = 5_000;
        int expectedIds = threads * idsPerThread;

        Set<Long> ids = ConcurrentHashMap.newKeySet();

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < idsPerThread; j++) {
                    ids.add(generator.generateId());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(expectedIds, ids.size());
    }
}

