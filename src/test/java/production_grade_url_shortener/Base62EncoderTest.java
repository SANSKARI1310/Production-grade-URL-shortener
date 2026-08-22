package production_grade_url_shortener;

import org.junit.jupiter.api.Test;
import production_grade_url_shortener.util.Base62Encoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class Base62EncoderTest {
    
    @Test
    void shouldEncodeLongToBase62() throws InterruptedException 
        {
            Base62Encoder base62Encoder = new Base62Encoder();
            assertEquals(12345, base62Encoder.decode(base62Encoder.encode(12345)));
        }
    }
