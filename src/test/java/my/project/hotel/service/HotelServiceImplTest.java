package my.project.hotel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import my.project.hotel.database.entity.Amenity;
import my.project.hotel.database.entity.Hotel;
import my.project.hotel.database.repository.AmenityRepository;
import my.project.hotel.database.repository.HotelRepository;
import my.project.hotel.dto.*;
import my.project.hotel.exception.EmptyAmenitiesException;
import my.project.hotel.exception.HotelCreationException;
import my.project.hotel.exception.HotelNotFoundException;
import my.project.hotel.exception.InvalidHistogramParamException;
import my.project.hotel.mapper.HotelCreateMapper;
import my.project.hotel.mapper.HotelFullMapper;
import my.project.hotel.mapper.HotelShortMapper;
import my.project.hotel.service.histogram.HistogramStrategy;
import my.project.hotel.service.histogram.HistogramStrategyFactory;
import my.project.hotel.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @Mock
    private HotelShortMapper hotelShortMapper;

    @Mock
    private HotelFullMapper hotelFullMapper;

    @Mock
    private HotelCreateMapper hotelCreateMapper;

    @Mock
    private HistogramStrategyFactory histogramStrategyFactory;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAllHotels_returnsMappedList() {
        Hotel hotel1 = new Hotel();
        hotel1.setId(1L);
        Hotel hotel2 = new Hotel();
        hotel2.setId(2L);

        when(hotelRepository.findAll()).thenReturn(List.of(hotel1, hotel2));
        when(hotelShortMapper.map(hotel1)).thenReturn(new HotelShortDto(1L, "H1", "Desc1", "Addr1", "123"));
        when(hotelShortMapper.map(hotel2)).thenReturn(new HotelShortDto(2L, "H2", "Desc2", "Addr2", "456"));

        List<HotelShortDto> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(hotelRepository).findAll();
        verify(hotelShortMapper, times(2)).map(any());
    }

    @Test
    void getHotelById_existingId_returnsFullDto() {
        Long id = 10L;
        Hotel hotel = new Hotel();
        hotel.setId(id);

        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotel));
        when(hotelFullMapper.map(hotel)).thenReturn(new HotelFullDto(id, "Name", "Desc", "Brand", null, null, null, List.of()));

        HotelFullDto dto = hotelService.getHotelById(id);

        assertNotNull(dto);
        assertEquals(id, dto.id());
        verify(hotelRepository).findById(id);
        verify(hotelFullMapper).map(hotel);
    }

    @Test
    void getHotelById_notFound_throwsException() {
        Long id = 5L;
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.getHotelById(id));
        verify(hotelRepository).findById(id);
        verifyNoMoreInteractions(hotelFullMapper);
    }

    @Test
    void createHotel_success_returnsFullDto() {
        // given
        HotelCreateDto createDto = mock(HotelCreateDto.class);
        Hotel hotel = new Hotel();
        Hotel savedHotel = new Hotel();
        savedHotel.setId(100L);

        HotelFullDto expectedDto = new HotelFullDto(
                100L,
                "Hotel Name",
                "Description",
                "Brand",
                new AddressDto(1, "Street", "City", "Country", "110"),
                new ContactsDto("123456789", "email@example.com"),
                new ArrivalTimeDto("14:00", "12:00"),
                List.of("WiFi", "Pool")
        );

        when(hotelCreateMapper.map(createDto)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelFullMapper.map(savedHotel)).thenReturn(expectedDto);

        // when
        HotelFullDto result = hotelService.createHotel(createDto);

        // then
        assertNotNull(result);
        assertEquals(100L, result.id());
        assertEquals("Hotel Name", result.name());
        assertEquals("City", result.address().city());

        verify(hotelCreateMapper).map(createDto);
        verify(hotelRepository).save(hotel);
        verify(hotelFullMapper).map(savedHotel);
    }


    @Test
    void createHotel_error_throwsHotelCreationException() {
        HotelCreateDto createDto = mock(HotelCreateDto.class);
        when(hotelCreateMapper.map(createDto)).thenThrow(new RuntimeException("DB error"));

        assertThrows(HotelCreationException.class, () -> hotelService.createHotel(createDto));
    }

    @Test
    void addAmenities_successful() {
        Long hotelId = 1L;
        List<String> amenities = List.of("Pool", "Gym");

        Amenity poolAmenity = new Amenity(10L, "Pool", new HashSet<>());
        Amenity gymAmenity = new Amenity(11L, "Gym", new HashSet<>());

        Hotel hotel = new Hotel();
        hotel.setAmenities(new HashSet<>());
        hotel.setId(hotelId);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName("Pool")).thenReturn(Optional.of(poolAmenity));
        when(amenityRepository.findByName("Gym")).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(gymAmenity);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        hotelService.addAmenities(hotelId, amenities);

        verify(hotelRepository).findById(hotelId);
        verify(amenityRepository).findByName("Pool");
        verify(amenityRepository).findByName("Gym");
        verify(amenityRepository).save(any(Amenity.class));
        verify(hotelRepository).save(hotel);

        assertTrue(hotel.getAmenities().contains(poolAmenity));
        assertTrue(hotel.getAmenities().contains(gymAmenity));
    }

    @Test
    void addAmenities_emptyList_throwsEmptyAmenitiesException() {
        Long hotelId = 1L;
        List<String> emptyList = Collections.emptyList();

        assertThrows(EmptyAmenitiesException.class, () -> hotelService.addAmenities(hotelId, emptyList));
        assertThrows(EmptyAmenitiesException.class, () -> hotelService.addAmenities(hotelId, null));
    }

    @Test
    void addAmenities_hotelNotFound_throwsHotelNotFoundException() {
        Long hotelId = 1L;
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> hotelService.addAmenities(hotelId, List.of("WiFi")));
    }

    @Test
    void getHistogram_validParam_returnsHistogram() {
        String param = "brand";
        Map<String, Long> histogramMap = Map.of("Brand1", 5L, "Brand2", 3L);

        HistogramStrategy strategy = mock(HistogramStrategy.class);
        when(histogramStrategyFactory.getStrategy(param)).thenReturn(strategy);
        when(strategy.computeHistogram()).thenReturn(histogramMap);

        Map<String, Long> result = hotelService.getHistogram(param);

        assertEquals(histogramMap, result);
        verify(histogramStrategyFactory).getStrategy(param);
        verify(strategy).computeHistogram();
    }

    @Test
    void getHistogram_invalidParam_throwsInvalidHistogramParamException() {
        String param = "invalidParam";

        when(histogramStrategyFactory.getStrategy(param)).thenReturn(null);

        assertThrows(InvalidHistogramParamException.class, () -> hotelService.getHistogram(param));
        verify(histogramStrategyFactory).getStrategy(param);
    }
}
