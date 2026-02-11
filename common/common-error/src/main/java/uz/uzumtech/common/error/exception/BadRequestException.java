package uz.uzumtech.common.error.exception;

import org.springframework.http.HttpStatus;

import static uz.uzumtech.common.error.core.ErrorConstants.CODE_BAD_REQUEST;

public class BadRequestException extends CommonException {

    public BadRequestException(String message) {
        super(CODE_BAD_REQUEST, message, HttpStatus.BAD_REQUEST);
    }
}
