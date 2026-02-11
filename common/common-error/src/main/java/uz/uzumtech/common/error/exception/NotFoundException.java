package uz.uzumtech.common.error.exception;

import org.springframework.http.HttpStatus;

import static uz.uzumtech.common.error.core.ErrorConstants.CODE_NOT_FOUND;

public class NotFoundException extends CommonException {

    public NotFoundException(String message) {
        super(CODE_NOT_FOUND, message, HttpStatus.NOT_FOUND);
    }
}
