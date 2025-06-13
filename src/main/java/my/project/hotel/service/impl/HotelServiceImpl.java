package my.project.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.project.hotel.database.entity.Hotel;
import my.project.hotel.database.entity.Amenity;
import my.project.hotel.database.repository.HotelRepository;
import my.project.hotel.database.repository.AmenityRepository;
import my.project.hotel.service.histogram.HistogramStrategyFactory;
import my.project.hotel.service.HotelService;
import my.project.hotel.dto.*;
import my.project.hotel.exception.EmptyAmenitiesException;
import my.project.hotel.exception.HotelCreationException;
import my.project.hotel.exception.HotelNotFoundException;
import my.project.hotel.exception.InvalidHistogramParamException;
import my.project.hotel.mapper.*;
import my.project.hotel.service.histogram.HistogramStrategy;

import my.project.hotel.util.HotelSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    private final HotelShortMapper hotelShortMapper;
    private final HotelFullMapper hotelFullMapper;
    private final HotelCreateMapper hotelCreateMapper;

    private final HistogramStrategyFactory histogramStrategyFactory;

    @Override
    public List<HotelShortDto> getAllHotels() {
        log.info("Fetching all hotels");
        return hotelRepository.findAll()
                .stream()
                .map(hotelShortMapper::map)
                .toList();
    }

    @Override
    public HotelFullDto getHotelById(Long id) {
        log.info("Fetching hotel by id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));
        return hotelFullMapper.map(hotel);
    }

    @Override
    public List<HotelShortDto> searchHotels(HotelSearchDto searchDto) {
        log.info("Searching hotels with filters: {}", searchDto);

        Specification<Hotel> spec = HotelSpecifications.hasNameLike(searchDto.name())
                .and(HotelSpecifications.hasBrandLike(searchDto.brand()))
                .and(HotelSpecifications.hasCityLike(searchDto.city()))
                .and(HotelSpecifications.hasCountryLike(searchDto.country()))
                .and(HotelSpecifications.hasAmenities(searchDto.amenities()));

        List<HotelShortDto> result = hotelRepository.findAll(spec)
                .stream()
                .map(hotelShortMapper::map)
                .toList();

        log.info("Found {} hotels", result.size());
        return result;
    }

    @Override
    @Transactional
    public HotelFullDto createHotel(HotelCreateDto createDto) {
        try {
            log.info("Creating hotel: {}", createDto);
            Hotel hotel = hotelCreateMapper.map(createDto);
            Hotel saved = hotelRepository.save(hotel);
            return hotelFullMapper.map(saved);
        } catch (Exception e) {
            log.error("Error creating hotel", e);
            throw new HotelCreationException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void addAmenities(Long hotelId, List<String> amenities) {

        if (amenities == null || amenities.isEmpty()) {
            log.warn("Attempted to add empty amenities list to hotel id {}", hotelId);
            throw new EmptyAmenitiesException();
        }

        log.info("Adding amenities {} to hotel with id {}", amenities, hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        Set<Amenity> amenitySet = new HashSet<>(hotel.getAmenities());

        for (String name : amenities) {
            Amenity amenity = amenityRepository.findByName(name)
                    .orElseGet(() -> {
                        log.info("Creating new amenity: {}", name);
                        return amenityRepository.save(new Amenity(null, name, new HashSet<>()));
                    });
            amenitySet.add(amenity);
        }

        hotel.setAmenities(amenitySet);
        hotelRepository.save(hotel);
        log.info("Updated amenities for hotel id {}", hotelId);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        log.info("Generating histogram for param: {}", param);
        HistogramStrategy strategy = histogramStrategyFactory.getStrategy(param);

        if (strategy == null) {
            log.error("Invalid histogram param: {}", param);
            throw new InvalidHistogramParamException(param);
        }

        return strategy.computeHistogram();
    }
}
