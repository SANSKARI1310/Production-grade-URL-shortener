package production_grade_url_shortener.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import production_grade_url_shortener.dto.RegisterUserRequest;
import production_grade_url_shortener.dto.RegisterUserResponse;
import production_grade_url_shortener.service.UserOnboardingService;
import org.springframework.http.HttpStatus;

@RestController 
@RequestMapping("/api/auth")
public class RegistorController {
    
    private final UserOnboardingService userOnboardingService;
    public RegistorController(UserOnboardingService userOnboardingService)
    {
        this.userOnboardingService = userOnboardingService;
    }   
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request)
    {
        RegisterUserResponse response = userOnboardingService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
