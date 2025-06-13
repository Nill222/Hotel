package my.project.hotel.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Property View API")
                        .description("API for hotel property management and search")
                        .version("1.0.0"));
    }
}
