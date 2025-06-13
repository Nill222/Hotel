package my.project.hotel.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacts implements BaseEntity<Long> {

    @Id
    private Long id;

    private String phone;
    private String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}

