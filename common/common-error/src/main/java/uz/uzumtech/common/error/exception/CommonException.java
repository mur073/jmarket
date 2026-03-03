package uz.uzumtech.common.error.exception;

import lombok.ToString;
import org.springframework.http.HttpStatus;
import uz.uzumtech.common.error.core.AbstractCommonException;
import uz.uzumtech.common.error.core.ErrorData;

@ToString
public class CommonException extends AbstractCommonException {

    public CommonException() {
    }

    public CommonException(Integer code, String message, HttpStatus status) {
        super(code, message, status);
    }

    public CommonException(Integer code, String message) {
        super(code, message);
    }

    public CommonException(ErrorData errorData) {
        super(errorData);
    }
}
