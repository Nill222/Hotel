package my.project.hotel.service.histogram.impl;

import lombok.RequiredArgsConstructor;
import my.project.hotel.database.repository.HotelRepository;
import my.project.hotel.service.histogram.HistogramStrategy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("amenitiesHistogramStrategy")
@RequiredArgsConstructor
public class AmenitiesHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public Map<String, Long> computeHistogram() {
        return hotelRepository.findAll()
                .stream()
                .flatMap(hotel ->
                        Optional.ofNullable(hotel.getAmenities()).stream()
                        .flatMap(Collection::stream))
                .map(amenity ->
                        Optional.ofNullable(amenity.getName()).orElse("Unknown"))
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()));
    }
}
