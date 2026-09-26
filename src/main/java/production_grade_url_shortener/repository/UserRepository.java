package production_grade_url_shortener.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import production_grade_url_shortener.entity.Users;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
    
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
