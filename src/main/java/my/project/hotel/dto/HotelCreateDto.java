package my.project.hotel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HotelCreateDto(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Description must not be blank")
        String description,

        @NotBlank(message = "Brand must not be blank")
        String brand,

        @NotNull(message = "Address must not be null")
        @Valid
        AddressDto address,

        @NotNull(message = "Contacts must not be null")
        @Valid
        ContactsDto contacts,

        @NotNull(message = "Arrival time must not be null")
        @Valid
        ArrivalTimeDto arrivalTime,

        @NotNull(message = "Amenities list must not be null")
        @NotEmpty(message = "Amenities list must not be empty")
        List<@NotBlank(message = "Amenity name must not be blank") String> amenities
) {}
