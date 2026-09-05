package production_grade_url_shortener.dto;

import java.util.List;

public record AnalyticsResponse
(
    String shortCode,
    long totalClicks,
    List<ClickDetailsDto> recentClicks
){}
