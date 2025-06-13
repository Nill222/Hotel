package my.project.hotel.service;

import my.project.hotel.dto.*;

import java.util.List;
import java.util.Map;

public interface HotelService {
    List<HotelShortDto> getAllHotels();
    HotelFullDto getHotelById(Long id);
    List<HotelShortDto> searchHotels(HotelSearchDto searchDto);
    HotelFullDto createHotel(HotelCreateDto createDto);
    void addAmenities(Long hotelId, List<String> amenities);
    Map<String, Long> getHistogram(String param);
}

