package my.project.hotel.mapper;

import my.project.hotel.database.entity.Address;
import my.project.hotel.dto.AddressDto;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper implements Mapper<AddressDto, Address> {

    @Override
    public Address map(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setHouseNumber(dto.houseNumber());
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setCountry(dto.country());
        address.setPostCode(dto.postCode());
        return address;
    }

    public AddressDto map(Address entity) {
        if (entity == null) {
            return null;
        }
        return new AddressDto(
                entity.getHouseNumber(),
                entity.getStreet(),
                entity.getCity(),
                entity.getCountry(),
                entity.getPostCode()
        );
    }
}
