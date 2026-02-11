package uz.uzumtech.common.error.dto;

import lombok.Builder;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uz.uzumtech.common.error.core.ErrorConstants.CODE_DEFAULT;

@Builder
public record ErrorResponse(
        Integer code,
        String message,
        Integer status
) {

    public static ErrorResponse of(String message) {
        return new ErrorResponse(CODE_DEFAULT, message, BAD_REQUEST.value());
    }
}
