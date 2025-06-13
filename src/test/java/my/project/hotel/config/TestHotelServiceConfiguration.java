package my.project.hotel.config;

import my.project.hotel.service.HotelService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestHotelServiceConfiguration {

    @Bean
    public HotelService hotelService() {
        return Mockito.mock(HotelService.class);
    }
}

