package my.project.hotel.mapper;

import my.project.hotel.database.entity.Contacts;
import my.project.hotel.dto.ContactsDto;
import org.springframework.stereotype.Component;

@Component
public class ContactsMapper implements Mapper<ContactsDto, Contacts> {

    @Override
    public Contacts map(ContactsDto dto) {
        if (dto == null) {
            return null;
        }
        Contacts contacts = new Contacts();
        contacts.setPhone(dto.phone());
        contacts.setEmail(dto.email());
        return contacts;
    }

    public ContactsDto map(Contacts entity) {
        if (entity == null) {
            return null;
        }
        return new ContactsDto(
                entity.getPhone(),
                entity.getEmail()
        );
    }
}
