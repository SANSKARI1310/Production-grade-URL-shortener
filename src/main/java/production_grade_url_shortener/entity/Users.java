package production_grade_url_shortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.Instant;

@Entity
@Table(name = "users")
public class Users {
    
    @Id 
    @Column(name = "id" , nullable = false , updatable = false , length = 64)
    private String id;

    @Column(name = "email" , nullable = false , updatable = false , length = 255)
    private String email;

    @Column(name = "created_at" , nullable = false , updatable = false)
    private Instant createdAt;

    protected Users() {
    }

    public Users(String id , String email , Instant createdAt)
    {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public Instant getCreatedAt() {
        return createdAt;     
    }


}
