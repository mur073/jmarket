package uz.uzumtech.common.error.exception;


import org.springframework.http.HttpStatus;

import static uz.uzumtech.common.error.core.ErrorConstants.CODE_UNAUTHORIZED;

public class UnauthorizedException extends CommonException {

    public UnauthorizedException(String message) {
        super(CODE_UNAUTHORIZED, message, HttpStatus.UNAUTHORIZED);
    }
}
