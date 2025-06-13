package my.project.hotel.config;

import lombok.Data;

@Data
public class DbConnectionProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}