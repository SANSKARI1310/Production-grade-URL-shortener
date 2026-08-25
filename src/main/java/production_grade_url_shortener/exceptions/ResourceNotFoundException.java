package production_grade_url_shortener.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message)
    {
        super(message);
      
    }



}
