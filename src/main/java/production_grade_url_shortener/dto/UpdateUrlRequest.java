package production_grade_url_shortener.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateUrlRequest {
    
    @NotBlank
    private String originalUrl;

    public UpdateUrlRequest(){
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
    
}
