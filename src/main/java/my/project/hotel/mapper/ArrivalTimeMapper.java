package my.project.hotel.mapper;

import my.project.hotel.database.entity.ArrivalTime;
import my.project.hotel.dto.ArrivalTimeDto;
import org.springframework.stereotype.Component;

@Component
public class ArrivalTimeMapper implements Mapper<ArrivalTimeDto, ArrivalTime> {

    @Override
    public ArrivalTime map(ArrivalTimeDto dto) {
        if (dto == null) {
            return null;
        }
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(dto.checkIn());
        arrivalTime.setCheckOut(dto.checkOut());
        return arrivalTime;
    }

    public ArrivalTimeDto map(ArrivalTime entity) {
        if (entity == null) {
            return null;
        }
        return new ArrivalTimeDto(
                entity.getCheckIn(),
                entity.getCheckOut()
        );
    }
}
