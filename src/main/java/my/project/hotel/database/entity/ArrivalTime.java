package my.project.hotel.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTime implements BaseEntity<Long>{

    @Id
    private Long id;

    private String checkIn;
    private String checkOut;

    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}

