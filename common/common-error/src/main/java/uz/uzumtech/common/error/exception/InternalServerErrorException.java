package uz.uzumtech.common.error.exception;

import org.springframework.http.HttpStatus;

import static uz.uzumtech.common.error.core.ErrorConstants.CODE_INTERNAL_SERVER_ERROR;

public class InternalServerErrorException extends CommonException {

    public InternalServerErrorException(String message) {
        super(CODE_INTERNAL_SERVER_ERROR, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
