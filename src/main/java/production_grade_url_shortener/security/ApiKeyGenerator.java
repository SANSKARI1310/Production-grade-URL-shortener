package production_grade_url_shortener.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Base64;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class ApiKeyGenerator {
    
    private static final String PREFIX = "pgurl_live_";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public record GeneratedApiKey(String rawKey,String keyHash , String keyPrefix){}

    public GeneratedApiKey generateApiKey()
    {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);

        String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String rawKey = PREFIX + secret;

        String keyPrefix = rawKey.substring(0, 16);

        String keyHash = hashKey(rawKey);

        return new GeneratedApiKey(rawKey , keyHash , keyPrefix);
    }

    public String hashKey(String rawKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm unavailable", e);
        }
    }

}
