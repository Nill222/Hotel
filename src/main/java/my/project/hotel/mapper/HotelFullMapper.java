package my.project.hotel.mapper;

import lombok.RequiredArgsConstructor;
import my.project.hotel.database.entity.Amenity;
import my.project.hotel.database.entity.Hotel;
import my.project.hotel.dto.AddressDto;
import my.project.hotel.dto.ArrivalTimeDto;
import my.project.hotel.dto.ContactsDto;
import my.project.hotel.dto.HotelFullDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HotelFullMapper implements Mapper<Hotel, HotelFullDto> {

    private final AddressMapper addressMapper;
    private final ContactsMapper contactsMapper;
    private final ArrivalTimeMapper arrivalTimeMapper;

    @Override
    public HotelFullDto map(Hotel object) {
        AddressDto address = Optional.ofNullable(object.getAddress())
                .map(addressMapper::map)
                .orElse(null);

        ContactsDto contacts = Optional.ofNullable(object.getContacts())
                .map(contactsMapper::map)
                .orElse(null);

        ArrivalTimeDto arrivalTime = Optional.ofNullable(object.getArrivalTime())
                .map(arrivalTimeMapper::map)
                .orElse(null);


        List<String> amenities = object.getAmenities()
                .stream()
                .map(Amenity::getName)
                .toList();

        return new HotelFullDto(
                object.getId(),
                object.getName(),
                object.getDescription(),
                object.getBrand(),
                address,
                contacts,
                arrivalTime,
                amenities
        );
    }
}
