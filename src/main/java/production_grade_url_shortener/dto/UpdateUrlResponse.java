package production_grade_url_shortener.dto;

import java.time.Instant;
public class UpdateUrlResponse {
    
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;

    public UpdateUrlResponse(String originalUrl , Instant createdAt , Instant expiresAt)
    {
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
    public String getOriginalUrl()
    {
        return originalUrl;
    }
    public Instant getCreatedAt()
    {
        return createdAt;
    }
    public Instant getExpiresAt()
    {
        return expiresAt;
    }

}
