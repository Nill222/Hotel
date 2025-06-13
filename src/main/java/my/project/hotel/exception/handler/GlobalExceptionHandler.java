package my.project.hotel.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import my.project.hotel.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Обработка валидации с аннотациями @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage())
        );

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleHotelNotFound(HotelNotFoundException ex) {
        log.warn("Hotel not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HotelCreationException.class)
    public ResponseEntity<ApiErrorResponse> handleHotelCreation(HotelCreationException ex) {
        log.error("Hotel creation failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(EmptyAmenitiesException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyAmenities(EmptyAmenitiesException ex) {
        log.warn("Empty amenities error: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidHistogramParamException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidHistogram(InvalidHistogramParamException ex) {
        log.warn("Invalid histogram param: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleOtherExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message) {
        ApiErrorResponse body = new ApiErrorResponse(status.value(), message);
        return new ResponseEntity<>(body, status);
    }

    @Getter
    public static class ApiErrorResponse {
        private final int status;
        private final String message;
        private Map<String, String> errors;

        public ApiErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public ApiErrorResponse(int status, String message, Map<String, String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }
    }
}
