package production_grade_url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
public class CreateUrlRequest {
    
    @NotBlank
    private String originalUrl;

    private Instant expiresAt;
    public CreateUrlRequest()
    {
            }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

}
