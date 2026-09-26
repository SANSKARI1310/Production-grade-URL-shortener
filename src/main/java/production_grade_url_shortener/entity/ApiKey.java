package production_grade_url_shortener.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity 
@Table (name = "api_keys")
public class ApiKey {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_hash" , nullable = false)
    private String apiHash;

    @Column(name = "user_id" , nullable = false)
    private String userId;

    @Column(name = "name" , nullable = false , length = 64)
    private String name;

    @Column(name = "key_prefix" , nullable = false , length = 16)
    private String keyPrefix;

    @Column(name = "is_active_api" , nullable = false)
    private boolean isActiveApi;

    @Column(name = "created_at" , nullable = false)
    private Instant createdAt;

    @Column(name = "last_used_at")
    private Instant lastUsedAt;

    protected ApiKey() {
    }

    public ApiKey( String apiHash , String userId , boolean isActiveApi , Instant createdAt , Instant lastUsedAt , String name , String keyPrefix)
    {
        this.apiHash = apiHash;
        this.userId = userId;
        this.isActiveApi = isActiveApi;
        this.createdAt = createdAt;
        this.lastUsedAt = lastUsedAt;
        this.name = name;
        this.keyPrefix = keyPrefix;
    }

    public Long getId() {
        return id;
    }
    public String getApiHash() {
        return apiHash;
    }
    public String getUserId() {
        return userId;
    }
    public boolean isActiveApi() {
        return isActiveApi;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getLastUsedAt() {
        return lastUsedAt;
    }
    public String getName() {
        return name;
    }
    public String getKeyPrefix() {
        return keyPrefix;
    }
    public void setLastUsedAt(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }
    public void deactivate()
    {
        this.isActiveApi = false;
    }   
}
