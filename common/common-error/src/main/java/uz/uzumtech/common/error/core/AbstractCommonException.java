package uz.uzumtech.common.error.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class AbstractCommonException extends RuntimeException {

    private Integer code;
    private String message;

    @JsonDeserialize(using = IntegerToHttpStatusConverter.class)
    private HttpStatus status;

    public AbstractCommonException() {
    }

    public AbstractCommonException(Integer code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public AbstractCommonException(Integer code, String message) {
        this(code, message, HttpStatus.BAD_REQUEST);
    }

    public AbstractCommonException(ErrorData errorData) {
        this.code = errorData.getCode();
        this.message = errorData.getMessage();
        this.status = errorData.getStatus();
    }
}
