package production_grade_url_shortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import production_grade_url_shortener.dto.CreateUrlRequest;
import production_grade_url_shortener.dto.CreateUrlResponse;
import production_grade_url_shortener.service.UrlService;
import production_grade_url_shortener.dto.UpdateUrlRequest;
import production_grade_url_shortener.dto.UpdateUrlResponse;


@RestController
@RequestMapping("/api/urls")
public class UrlController {
    
     private UrlService urlService;
    
     public UrlController(UrlService urlService)
        {
            this.urlService = urlService;
        }
    @PostMapping
    public ResponseEntity<CreateUrlResponse> createUrl( @Valid @RequestBody CreateUrlRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.createUrl(request));
    } 
    @PatchMapping("/{shortcode}")
    public ResponseEntity<UpdateUrlResponse> updateUrl(@PathVariable String shortcode , @Valid @RequestBody UpdateUrlRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(urlService.updateUrl(shortcode , request));
    }   
    @DeleteMapping("/{shortcode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortcode)
    {
        urlService.deleteUrl(shortcode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
