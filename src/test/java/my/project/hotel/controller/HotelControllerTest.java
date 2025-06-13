package my.project.hotel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.hotel.config.TestHotelServiceConfiguration;
import my.project.hotel.dto.*;
import my.project.hotel.http.controller.HotelController;
import my.project.hotel.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(HotelController.class)
@Import(TestHotelServiceConfiguration.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllHotels_returnsHotelList() throws Exception {
        var hotelDto = new HotelShortDto(1L, "Hotel One", "Brand", "City", "Country");
        Mockito.when(hotelService.getAllHotels()).thenReturn(List.of(hotelDto));

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getHotelById_returnsHotel() throws Exception {
        var fullDto = new HotelFullDto(1L, "Hotel One", "Nice hotel", "Brand",
                new AddressDto(2, "Country", "City", "Street", "10"),
                new ContactsDto("123456789", "email@example.com"),
                new ArrivalTimeDto("14:00", "12:00"), List.of("WiFi"));

        Mockito.when(hotelService.getHotelById(1L)).thenReturn(fullDto);

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void searchHotels_withParams_returnsFilteredList() throws Exception {
        var hotelDto = new HotelShortDto(2L, "Hotel Two", "Brand", "City", "Country");

        Mockito.when(hotelService.searchHotels(any(HotelSearchDto.class)))
                .thenReturn(List.of(hotelDto));

        mockMvc.perform(get("/property-view/search")
                        .param("name", "Hotel")
                        .param("brand", "Brand")
                        .param("city", "City")
                        .param("country", "Country")
                        .param("amenities", "WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L));
    }

    @Test
    void createHotel_returnsCreatedHotel() throws Exception {
        var createDto = new HotelCreateDto(
                "Hotel Three",
                "Nice place",
                "Brand",
                new AddressDto(1, "Street", "City", "Country", "110"),
                new ContactsDto("123456789", "email@example.com"),
                new ArrivalTimeDto("14:00", "12:00"),
                List.of("WiFi", "Pool")
        );

        var hotelDto = new HotelFullDto(
                3L,
                "Hotel Three",
                "Nice place",
                "Brand",
                new AddressDto(1, "Street", "City", "Country", "110"),
                new ContactsDto("123456789", "email@example.com"),
                new ArrivalTimeDto("14:00", "12:00"),
                List.of("WiFi", "Pool")
        );

        Mockito.when(hotelService.createHotel(any(HotelCreateDto.class))).thenReturn(hotelDto);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Hotel Three"))
                .andExpect(jsonPath("$.brand").value("Brand"))
                .andExpect(jsonPath("$.description").value("Nice place"))
                // Проверяем вложенные поля адреса
                .andExpect(jsonPath("$.address.city").value("City"))
                .andExpect(jsonPath("$.address.country").value("Country"))
                // Проверяем вложенные поля контактов
                .andExpect(jsonPath("$.contacts.phone").value("123456789"))
                .andExpect(jsonPath("$.contacts.email").value("email@example.com"))
                // Проверяем arrivalTime
                .andExpect(jsonPath("$.arrivalTime.checkIn").value("14:00"))
                .andExpect(jsonPath("$.arrivalTime.checkOut").value("12:00"))
                // Проверяем amenities массив
                .andExpect(jsonPath("$.amenities").isArray())
                .andExpect(jsonPath("$.amenities[0]").value("WiFi"))
                .andExpect(jsonPath("$.amenities[1]").value("Pool"));
    }


    @Test
    void addAmenities_returnsNoContent() throws Exception {
        List<String> amenities = List.of("WiFi", "Pool");

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isNoContent());

        Mockito.verify(hotelService).addAmenities(eq(1L), eq(amenities));
    }

    @Test
    void getHistogram_returnsHistogram() throws Exception {
        Map<String, Long> histogram = Map.of("BrandA", 3L, "BrandB", 5L);

        Mockito.when(hotelService.getHistogram("brand")).thenReturn(histogram);

        mockMvc.perform(get("/property-view/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.BrandA").value(3))
                .andExpect(jsonPath("$.BrandB").value(5));
    }
}
