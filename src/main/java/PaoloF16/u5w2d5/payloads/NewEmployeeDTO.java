package PaoloF16.u5w2d5.payloads;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewEmployeeDTO(
        @NotEmpty(message = "First Name cannot be empty.")
        @Size(min = 2, max = 20, message = "First Name should be 2 to 20 characters long")
        String firstName,
        @NotEmpty(message = "Last Name cannot be empty.")
        @Size(min = 2, max = 20, message = "Last Name should be 2 to 20 characters long")
        String lastName,
        @NotEmpty(message = "Email cannot be empty.")
        @Email(message = "Please enter a valid email")
        String email,
        @NotEmpty(message = "Username cannot be empty")
        String username) {
}