package production_grade_url_shortener.dto;

import java.time.Instant;

public record ClickDetailsDto (
    String ipAddress,
    String userAgent,
    String referer,
    Instant clickedAt
){}
