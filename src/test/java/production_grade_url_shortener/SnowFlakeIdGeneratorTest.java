package production_grade_url_shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;
import production_grade_url_shortener.util.IdGenerator;
import production_grade_url_shortener.util.SnowFlakeIdgenerator;

import org.junit.jupiter.api.Test;

public class SnowFlakeIdGeneratorTest {
    
    @Test
    void shouldGenerateUniqueIds() {

        IdGenerator generator = new SnowFlakeIdgenerator(1);

        Set<Long> ids = new HashSet<>();

        for (int i = 0; i < 100_000; i++) {
            ids.add(generator.generateId());
        }

        assertEquals(100_000, ids.size());
    }
}
