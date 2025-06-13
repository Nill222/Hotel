package my.project.hotel.exception;

public class InvalidHistogramParamException extends RuntimeException {
    public InvalidHistogramParamException(String param) {
        super("Invalid histogram parameter: " + param);
    }
}
