package my.project.hotel.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import my.project.hotel.database.entity.Hotel;
import java.util.List;

import my.project.hotel.database.entity.Amenity;

public class HotelSpecifications {

    public static Specification<Hotel> hasNameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return cb.conjunction(); // true
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> hasBrandLike(String brand) {
        return (root, query, cb) -> {
            if (brand == null || brand.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> hasCityLike(String city) {
        return (root, query, cb) -> {
            if (city == null || city.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address").get("city")), "%" + city.toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> hasCountryLike(String country) {
        return (root, query, cb) -> {
            if (country == null || country.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%");
        };
    }

    public static Specification<Hotel> hasAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }

            query.distinct(true);

            Join<Hotel, Amenity> join = root.join("amenities", JoinType.LEFT);
            return join.get("name").in(amenities);
        };
    }
}

