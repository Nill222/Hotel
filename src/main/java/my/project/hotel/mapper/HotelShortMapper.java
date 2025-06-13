package my.project.hotel.mapper;

import lombok.RequiredArgsConstructor;
import my.project.hotel.database.entity.Hotel;
import my.project.hotel.dto.HotelShortDto;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelShortMapper implements Mapper<Hotel, HotelShortDto> {

    @Override
    public HotelShortDto map(Hotel hotel) {
        String formattedAddress = hotel.getAddress().getHouseNumber() + " " +
                hotel.getAddress().getStreet() + ", " +
                hotel.getAddress().getCity() + ", " +
                hotel.getAddress().getPostCode() + ", " +
                hotel.getAddress().getCountry();

        return new HotelShortDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                formattedAddress,
                hotel.getContacts().getPhone()
        );
    }
}