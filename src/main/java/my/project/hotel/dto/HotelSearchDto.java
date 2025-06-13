package my.project.hotel.dto;

import java.util.List;

public record HotelSearchDto(
        String name,
        String brand,
        String city,
        String country,
        List<String> amenities
) {}