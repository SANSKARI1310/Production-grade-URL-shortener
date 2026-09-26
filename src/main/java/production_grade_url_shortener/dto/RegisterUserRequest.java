package production_grade_url_shortener.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
    @NotBlank(message = "Email should not be blank")
    @Email(message ="Invalid email format")
    String email,

    @NotBlank(message = "Name should not be blank")
    @Size(max = 50 , message = "Name should not be more than 50 characters")
    String name

)
{
}