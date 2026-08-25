package production_grade_url_shortener.dto;

import java.time.Instant;
public class CreateUrlResponse {
    
    private String shortcode;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;


    public CreateUrlResponse(String shortcode , String originalUrl , Instant createdAt , Instant expiresAt)
    {
        this.shortcode = shortcode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getShortcode()
    {
        return shortcode;
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
