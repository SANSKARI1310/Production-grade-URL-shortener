package production_grade_url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import production_grade_url_shortener.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey , Long>{

    Optional<ApiKey> findByApiHashAndIsActiveApi(String apiHash);

}
