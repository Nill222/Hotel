package my.project.hotel.http.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.project.hotel.service.HotelService;
import my.project.hotel.dto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hotel API", description = "Управление отелями и поиск")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Получить список всех отелей", description = "Возвращает список краткой информации обо всех отелях")
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortDto>> getAllHotels() {
        log.info("GET /hotels");
        List<HotelShortDto> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Получить подробную информацию об отеле по ID", description = "Возвращает полный объект отеля по его уникальному идентификатору")
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullDto> getHotelById(
            @Parameter(description = "ID отеля", example = "1", required = true)
            @PathVariable Long id) {
        log.info("GET /hotels/{}", id);
        HotelFullDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @Operation(summary = "Поиск отелей по фильтрам", description = "Поиск отелей по названию, бренду, городу, стране и удобствам")
    @GetMapping("/search")
    public ResponseEntity<List<HotelShortDto>> searchHotels(
            @Parameter(description = "Название отеля") @RequestParam(required = false) String name,
            @Parameter(description = "Бренд отеля") @RequestParam(required = false) String brand,
            @Parameter(description = "Город расположения отеля") @RequestParam(required = false) String city,
            @Parameter(description = "Страна расположения отеля") @RequestParam(required = false) String country,
            @Parameter(description = "Список удобств (amenities)") @RequestParam(required = false) List<String> amenities
    ) {
        log.info("GET /search with filters: name={}, brand={}, city={}, country={}, amenities={}",
                name, brand, city, country, amenities);

        HotelSearchDto searchDto = new HotelSearchDto(name, brand, city, country, amenities);
        List<HotelShortDto> result = hotelService.searchHotels(searchDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Создать новый отель", description = "Добавляет новый отель по данным в теле запроса")
    @PostMapping("/hotels")
    public ResponseEntity<HotelFullDto> createHotel(
            @Parameter(description = "Данные для создания отеля", required = true)
            @Valid @RequestBody HotelCreateDto createDto) {
        log.info("POST /hotels with body: {}", createDto);
        HotelFullDto createdHotel = hotelService.createHotel(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @Operation(summary = "Добавить удобства к отелю", description = "Добавляет список удобств к существующему отелю по ID")
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenities(
            @Parameter(description = "ID отеля", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody List<String> amenities) {
        log.info("POST /hotels/{}/amenities with amenities: {}", id, amenities);
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Получить гистограмму по параметру", description = "Возвращает статистическую гистограмму по указанному параметру (например, бренду, городу)")
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(
            @Parameter(description = "Параметр для построения гистограммы", example = "brand", required = true)
            @PathVariable String param) {
        log.info("GET /histogram/{}", param);
        Map<String, Long> histogram = hotelService.getHistogram(param);
        return ResponseEntity.ok(histogram);
    }
}
