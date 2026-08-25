package production_grade_url_shortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "urls")

public class Url {
    
    @Id
    @Column(name = "id" , nullable = false , updatable = false)
    private Long id;
    
    @Column(name = "short_code" , nullable = false , updatable = false , length = 16)
    private String shortcode;

    @Column(name = "original_url" , nullable = false , updatable = false)
    private String originalUrl;

    @Column(name = "created_at" , nullable = false , updatable = false)
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "is_active" , nullable = false )
    private boolean isActive;

    protected Url() {
    }

    public Url(Long id , String shortcode , String originalUrl , Instant createdAt , Instant expiresAt)
    {
        this.id = Objects.requireNonNull(id ,"Id should not be null");
        this.shortcode = Objects.requireNonNull(shortcode ,"Shortcode should not be null");
        this.originalUrl = Objects.requireNonNull(originalUrl ,"OriginalUrl should not be null");
        this.createdAt = createdAt!=null?createdAt:Instant.now();
        this.expiresAt = expiresAt;
        this.isActive = true;

    }

    public Long getId()
    {
        return id;
    }
    public String getShortCode()
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
    public boolean isActive()
    {
        return isActive;
    }
    public boolean isExpired()
    {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }
    

}
