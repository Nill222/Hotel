package my.project.hotel.mapper;

import lombok.RequiredArgsConstructor;
import my.project.hotel.database.entity.*;
import my.project.hotel.database.repository.AmenityRepository;
import my.project.hotel.dto.HotelCreateDto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HotelCreateMapper implements Mapper<HotelCreateDto, Hotel> {

    private final AddressMapper addressMapper;
    private final ContactsMapper contactsMapper;
    private final ArrivalTimeMapper arrivalTimeMapper;
    private final AmenityRepository amenityRepository;

    @Override
    public Hotel map(HotelCreateDto dto) {
        Hotel hotel = new Hotel();
        copy(dto, hotel);
        return hotel;
    }

    @Override
    public Hotel map(HotelCreateDto fromObject, Hotel toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(HotelCreateDto dto, Hotel hotel) {
        hotel.setName(dto.name());
        hotel.setDescription(dto.description());
        hotel.setBrand(dto.brand());
        hotel.setAddress(mapAddress(dto));
        hotel.setContacts(mapContacts(dto));
        hotel.setArrivalTime(mapArrivalTime(dto));
        hotel.setAmenities(mapAmenities(dto.amenities()));
    }

    private Address mapAddress(HotelCreateDto dto) {
        return dto.address() != null ? addressMapper.map(dto.address()) : null;
    }

    private Contacts mapContacts(HotelCreateDto dto) {
        return dto.contacts() != null ? contactsMapper.map(dto.contacts()) : null;
    }

    private ArrivalTime mapArrivalTime(HotelCreateDto dto) {
        return dto.arrivalTime() != null ? arrivalTimeMapper.map(dto.arrivalTime()) : null;
    }

    private Set<Amenity> mapAmenities(List<String> names) {
        Set<Amenity> amenities = new HashSet<>();
        if (names == null) return amenities;

        for (String name : names) {
            Amenity amenity = amenityRepository.findByName(name)
                    .orElseGet(() -> amenityRepository.save(new Amenity(null, name, new HashSet<>())));
            amenities.add(amenity);
        }

        return amenities;
    }
}
