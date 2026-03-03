package uz.uzumtech.jmarket.integration.users.web.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import uz.uzumtech.common.error.AbstractExceptionHandler;
import uz.uzumtech.common.error.dto.ErrorResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_FORBIDDEN;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers extends AbstractExceptionHandler {

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

}
