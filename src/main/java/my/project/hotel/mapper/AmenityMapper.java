package my.project.hotel.mapper;

import my.project.hotel.database.entity.Amenity;
import my.project.hotel.dto.AmenityDto;
import org.springframework.stereotype.Component;

@Component
public class AmenityMapper {
    public AmenityDto map(Amenity amenity) {
        return new AmenityDto(amenity.getName());
    }
}

