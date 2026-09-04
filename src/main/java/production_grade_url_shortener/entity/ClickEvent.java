package production_grade_url_shortener.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Objects;


@Entity
@Table(name = "click_event")
public class ClickEvent {
    
    @Id
    private Long id;

    @Column(name = "short_code" , nullable = false)
    private String shortCode;

    @Column(name = "original_url" , nullable = false)
    private String originalUrl;
    
    @Column(name = "ip_address" )
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "referer")
    private String referer;

    @Column(name = "clicked_at" , nullable = false)
    private Instant clickedAt;

    protected ClickEvent() 
        {

        }
    
    public ClickEvent(Long id ,String shortCode,String originalUrl , String ipAddress , String userAgent , String referer , Instant clickedAt)
    {
        this.id = Objects.requireNonNull(id);
        this.shortCode = Objects.requireNonNull(shortCode);
        this.originalUrl = Objects.requireNonNull(originalUrl);
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referer = referer;
        this.clickedAt = Objects.requireNonNull(clickedAt);

    }

    public Long getId()
    {
        return id;
    }
    public String getOriginalUrl()
    {
        return originalUrl;
    }
    public String getIpAddress()
    {
        return ipAddress;
    }
    public String getUserAgent()
    {
        return userAgent;
    }
    public String getReferer()
    {
        return referer;
    }
    public Instant getClickedAt()
    {
        return clickedAt;
    }

}
