package production_grade_url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import production_grade_url_shortener.entity.ClickEvent;

public interface ClickEventRepository extends JpaRepository<ClickEvent , Long>{
    
}
