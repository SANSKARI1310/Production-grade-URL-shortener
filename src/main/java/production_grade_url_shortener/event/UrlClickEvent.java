package production_grade_url_shortener.event;
import java.time.Instant;

public record UrlClickEvent( 
    String originalUrl,
    String shortcode,
    String ipAddress,
    Instant timestamp
){}