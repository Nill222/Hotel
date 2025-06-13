package my.project.hotel.exception;

public class HotelCreationException extends RuntimeException {
    public HotelCreationException(String message) {
        super("Failed to create hotel: " + message);
    }
}
