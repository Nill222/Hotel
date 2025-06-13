package my.project.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactsDto(
        @NotBlank(message = "Phone must not be blank")
        @Pattern(regexp = "^\\+?[\\d\\s()-]{7,20}$", message = "Phone number format is invalid")
        String phone,

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email format is invalid")
        String email
) {}
