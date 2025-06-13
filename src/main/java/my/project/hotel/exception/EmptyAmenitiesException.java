package my.project.hotel.exception;

public class EmptyAmenitiesException extends RuntimeException {
    public EmptyAmenitiesException() {
        super("Amenities list cannot be null or empty");
    }
}

