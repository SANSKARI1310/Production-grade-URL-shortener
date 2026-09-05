package production_grade_url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import production_grade_url_shortener.entity.ClickEvent;
import java.util.List;

public interface ClickEventRepository extends JpaRepository<ClickEvent , Long>{
    
    boolean existsByEventId(String eventId);
    long countByShortCode(String shortCode);
    List<ClickEvent> findTop10ByShortCodeOrderByClickedAtDesc(String shortCode);
}
