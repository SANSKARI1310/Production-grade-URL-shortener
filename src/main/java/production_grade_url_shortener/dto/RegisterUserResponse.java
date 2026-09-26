package production_grade_url_shortener.dto;

import java.time.Instant;

public record RegisterUserResponse (
    String userId,
    String email,
    String apiKeyId,
    String apiKeyName,
    String rawApiKey,
    String keyPrefix,
    Instant createdAt,
    String notice
)
{}
