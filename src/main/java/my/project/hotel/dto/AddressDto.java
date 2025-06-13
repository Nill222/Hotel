package my.project.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotNull(message = "House number must not be null")
        @PositiveOrZero(message = "House number must be zero or positive")
        Integer houseNumber,

        @NotBlank(message = "Street must not be blank")
        String street,

        @NotBlank(message = "City must not be blank")
        String city,

        @NotBlank(message = "Country must not be blank")
        String country,

        @NotBlank(message = "Post code must not be blank")
        @Size(min = 3, max = 10, message = "Post code length must be between 3 and 10")
        String postCode
) {}
