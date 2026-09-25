package production_grade_url_shortener.dto;

public record RateLimitResult(
    boolean allowed,
    long remainingTokens,
    boolean degradedFallback
) {
    
}
