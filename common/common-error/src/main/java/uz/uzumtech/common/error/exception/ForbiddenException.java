package uz.uzumtech.common.error.exception;

import org.springframework.http.HttpStatus;

import static uz.uzumtech.common.error.core.ErrorConstants.CODE_FORBIDDEN;

public class ForbiddenException extends CommonException {

    public ForbiddenException(String message) {
        super(CODE_FORBIDDEN, message, HttpStatus.FORBIDDEN);
    }
}
