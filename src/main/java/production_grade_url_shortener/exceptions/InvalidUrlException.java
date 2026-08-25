package production_grade_url_shortener.exceptions;

public class InvalidUrlException extends RuntimeException {

    public InvalidUrlException(String message) 
        {
            super(message);
        }
    
}
