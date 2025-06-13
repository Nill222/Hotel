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
        if (dto == null) return null;

        Hotel hotel = Hotel.builder()
                .name(dto.name())
                .description(dto.description())
                .brand(dto.brand())
                .build();

        if (dto.address() != null) {
            Address address = addressMapper.map(dto.address());
            address.setHotel(hotel);
            hotel.setAddress(address);
        }

        if (dto.contacts() != null) {
            Contacts contacts = contactsMapper.map(dto.contacts());
            contacts.setHotel(hotel);
            hotel.setContacts(contacts);
        }

        if (dto.arrivalTime() != null) {
            ArrivalTime arrivalTime = arrivalTimeMapper.map(dto.arrivalTime());
            arrivalTime.setHotel(hotel);
            hotel.setArrivalTime(arrivalTime);
        }

        hotel.setAmenities(mapAmenities(dto.amenities()));

        return hotel;
    }

    @Override
    public Hotel map(HotelCreateDto fromObject, Hotel toObject) {
        throw new UnsupportedOperationException("Updating an existing Hotel is not supported in this mapper.");
    }

    private Set<Amenity> mapAmenities(List<String> names) {
        Set<Amenity> amenities = new HashSet<>();
        if (names == null || names.isEmpty()) return amenities;

        for (String name : names) {
            Amenity amenity = amenityRepository.findByName(name)
                    .orElseGet(() -> amenityRepository.save(
                            Amenity.builder().name(name).hotels(new HashSet<>()).build()));
            amenities.add(amenity);
        }

        return amenities;
    }
}
