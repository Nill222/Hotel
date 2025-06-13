package my.project.hotel.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address implements BaseEntity<Long>{

    @Id
    private Long id;

    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}

