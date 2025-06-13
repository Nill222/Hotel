package my.project.hotel.database.repository;

import my.project.hotel.database.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    List<Hotel> findByNameContainingIgnoreCase(String name);
    List<Hotel> findByBrandContainingIgnoreCase(String brand);
    List<Hotel> findByAddress_CityContainingIgnoreCase(String city);
    List<Hotel> findByAddress_CountryContainingIgnoreCase(String country);
    List<Hotel> findDistinctByAmenities_NameIn(List<String> amenities);
}

