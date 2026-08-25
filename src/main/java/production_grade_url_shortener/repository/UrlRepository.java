package production_grade_url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import production_grade_url_shortener.entity.Url;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url , Long>{

    Optional<Url> findByShortcode(String shortcode);
    
}
