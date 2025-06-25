package my.project.hotel.database.repository;

import lombok.NonNull;
import my.project.hotel.database.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    @Query("""
    SELECT DISTINCT h FROM Hotel h
    LEFT JOIN FETCH h.address
    LEFT JOIN FETCH h.contacts
    LEFT JOIN FETCH h.arrivalTime
    LEFT JOIN FETCH h.amenities
""")
    @NonNull List<Hotel> findAll();
}

