package uz.uzumtech.common.error.core;

import org.springframework.http.HttpStatus;

public interface ErrorData {

    String getMessage();

    Integer getCode();

    default HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
