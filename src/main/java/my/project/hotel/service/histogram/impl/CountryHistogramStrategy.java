package my.project.hotel.service.histogram.impl;

import lombok.RequiredArgsConstructor;
import my.project.hotel.database.entity.Address;
import my.project.hotel.database.repository.HotelRepository;
import my.project.hotel.service.histogram.HistogramStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("countryHistogramStrategy")
@RequiredArgsConstructor
public class CountryHistogramStrategy implements HistogramStrategy {

    private final HotelRepository hotelRepository;

    @Override
    public Map<String, Long> computeHistogram() {
        return hotelRepository.findAll()
                .stream()
                .map(hotel -> Optional.ofNullable(hotel.getAddress())
                        .map(Address::getCountry)
                        .orElse("Unknown"))
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }
}
