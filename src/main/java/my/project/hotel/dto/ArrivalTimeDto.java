package my.project.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ArrivalTimeDto(
        @NotBlank(message = "Check-in time must not be blank")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Check-in time must be in HH:mm format")
        String checkIn,

        @NotBlank(message = "Check-out time must not be blank")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "Check-out time must be in HH:mm format")
        String checkOut
) {}
