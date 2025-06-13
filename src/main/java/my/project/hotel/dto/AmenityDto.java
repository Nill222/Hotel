package my.project.hotel.dto;

import jakarta.validation.constraints.NotBlank;

public record AmenityDto(
        @NotBlank(message = "Amenity name must not be blank")
        String name
) {}
