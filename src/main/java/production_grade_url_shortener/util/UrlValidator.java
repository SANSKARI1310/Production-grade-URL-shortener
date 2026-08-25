package production_grade_url_shortener.util;
import production_grade_url_shortener.exceptions.InvalidUrlException;
import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

@Component
public class UrlValidator {
    
    public String validateAndNormalizeUrl(String rawUrl)
    {
        if(rawUrl == null || rawUrl.isEmpty() || rawUrl.length() > 2048)
        {
            throw new InvalidUrlException("Invalid URL");
        }
        URI uri;
        try
        {
            uri = new URI(rawUrl.trim());
        }
        catch(URISyntaxException e)
        {
            throw new InvalidUrlException("Invalid URL");
        }
        String scheme = uri.getScheme();
        if(scheme == null ||( !scheme.equalsIgnoreCase("HTTP") && !scheme.equalsIgnoreCase("HTTPS")))
        {
            throw new InvalidUrlException("Only HTTP and HTTPS are supported");
        }
        String host = uri.getHost();
        if(host == null || host.isEmpty())
        {   
            throw new InvalidUrlException("Url must contain a valid Host");
        }
        return uri.normalize().toString();
    }

}
