package production_grade_url_shortener.exceptions;

public class UrlNotFoundException extends RuntimeException {
    public UrlNotFoundException(String message)
    {
        super(message);
    }
}
