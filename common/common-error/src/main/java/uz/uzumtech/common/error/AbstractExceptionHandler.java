package uz.uzumtech.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import uz.uzumtech.common.error.dto.ErrorResponse;
import uz.uzumtech.common.error.exception.CommonException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_BAD_REQUEST;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_FORBIDDEN;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_INTERNAL_SERVER_ERROR;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_METHOD_NOT_ALLOWED;

@Slf4j
public abstract class AbstractExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
        log.warn("Caught CommonException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .status(ex.getStatus().value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    private ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.warn("Caught MissingRequestHeaderException : {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(CODE_BAD_REQUEST)
                .message(ex.getMessage())
                .status(BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.warn("Caught HttpMediaTypeNotSupportedException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(CODE_METHOD_NOT_ALLOWED)
                .message(ex.getMessage())
                .status(METHOD_NOT_ALLOWED.value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        log.warn("Caught MethodArgumentTypeMismatchException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.of(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.warn("Caught MethodArgumentNotValidException: {}", ex.getMessage());

        String message = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error instanceof FieldError fieldError ? fieldError.getField() + ": " + fieldError.getDefaultMessage() : error.getDefaultMessage())
                .findFirst()
                .orElse("Method arguments not valid");
        ErrorResponse response = ErrorResponse.of(message);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Caught AccessDeniedException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(CODE_FORBIDDEN)
                .message(ex.getMessage())
                .status(FORBIDDEN.value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.warn("Caught NoResourceFoundException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .code(CODE_BAD_REQUEST)
                .message("Resource not exists")
                .status(BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Caught RuntimeException: ", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(CODE_INTERNAL_SERVER_ERROR)
                .message("Something went wrong")
                .status(INTERNAL_SERVER_ERROR.value())
                .build();
        return ResponseEntity.status(response.status()).body(response);
    }
}
