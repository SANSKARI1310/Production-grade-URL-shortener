package production_grade_url_shortener.util;
import production_grade_url_shortener.exceptions.InvalidUrlException;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.net.UnknownHostException;

@Component
public class UrlValidator {
    
    @Value("${app.domain:localhost}")
    private String appDomain;

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
        // to prevent redirect / loop
        if (host.equalsIgnoreCase(appDomain)) {
            throw new InvalidUrlException("Shortening URLs targeting the service domain is prohibited");
        }

        validateHostNotPrivateOrReserved(host);
        return uri.normalize().toString();

    }

    private void validateHostNotPrivateOrReserved(String host)
    {
        String lowerHost = host.toLowerCase();
        if (lowerHost.equals("localhost") || lowerHost.endsWith(".localhost") || lowerHost.endsWith(".local")) {
            throw new InvalidUrlException("Target host is not permitted (local address)");
        }
        try {
            // Resolve all IPs associated with the host (handles IPv4 and IPv6)
            InetAddress[] addresses = InetAddress.getAllByName(host);

            for (InetAddress address : addresses) {
                if (isPrivateOrReserved(address)) {
                    throw new InvalidUrlException("Target host resolves to a restricted/private network address");
                }
            }
        } catch (UnknownHostException e) {
            // Host does not exist or cannot be resolved via DNS
            throw new InvalidUrlException("Target host could not be resolved");
        }
    }
    private boolean isPrivateOrReserved(InetAddress addr) {
        // Blocks 127.0.0.0/8, ::1 (loopback)
        if (addr.isLoopbackAddress()) {
            return true;
        }

        // Blocks 0.0.0.0, :: (wildcard)
        if (addr.isAnyLocalAddress()) {
            return true;
        }

        // Blocks 169.254.0.0/16, fe80::/10 (Link-Local, AWS/GCP/Azure IMDS 169.254.169.254)
        if (addr.isLinkLocalAddress()) {
            return true;
        }

        // Blocks 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16, fec0::/10 (RFC 1918 Private networks)
        if (addr.isSiteLocalAddress()) {
            return true;
        }

        // Blocks 224.0.0.0/4, ff00::/8 (Multicast)
        if (addr.isMulticastAddress()) {
            return true;
        }

        // Cloud Metadata specific checks (169.254.169.254 and IPv6 equivalent)
        String hostAddress = addr.getHostAddress();
        if ("169.254.169.254".equals(hostAddress) || "fd00:ec2::254".equalsIgnoreCase(hostAddress)) {
            return true;
        }

        return false;
    }
}


