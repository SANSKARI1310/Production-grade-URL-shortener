package production_grade_url_shortener.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import production_grade_url_shortener.dto.RegisterUserRequest;
import production_grade_url_shortener.dto.RegisterUserResponse;
import production_grade_url_shortener.entity.ApiKey;
import production_grade_url_shortener.entity.Users;
import production_grade_url_shortener.repository.ApiKeyRepository;
import production_grade_url_shortener.repository.UserRepository;
import production_grade_url_shortener.security.ApiKeyGenerator;
import java.util.UUID;

@Service 
public class UserOnboardingService {
    
    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGenerator apiKeyGenerator;
    public UserOnboardingService(UserRepository userRepository , ApiKeyRepository apiKeyRepository , ApiKeyGenerator apiKeyGenerator)
    {
        this.userRepository = userRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.apiKeyGenerator = apiKeyGenerator;
    }   

    @Transactional 
    public RegisterUserResponse registerUser(RegisterUserRequest request)
    {
        if(userRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("User with email already exists" + request.email());

        String userId = "usr_" + UUID.randomUUID().toString().replace("-" , "").substring(0 , 16);
        Users user = new Users(userId, request.email() , Instant.now());
        userRepository.save(user);

        ApiKeyGenerator.GeneratedApiKey keyData = apiKeyGenerator.generateApiKey();
        // need to fix this
        ApiKey apiKey = new ApiKey(keyData.keyHash() , user.getId() , true , Instant.now() , null , request.name() , keyData.keyPrefix());
        ApiKey savedKey = apiKeyRepository.save(apiKey);

        return new RegisterUserResponse(
            user.getId(),
            user.getEmail(),
            savedKey.getId().toString(),
            savedKey.getName(),
            keyData.rawKey(),// one time only 
            keyData.keyPrefix(),
            savedKey.getCreatedAt(),
            "Welcome to Production-grade URL Shortener"
        );

    }

}
