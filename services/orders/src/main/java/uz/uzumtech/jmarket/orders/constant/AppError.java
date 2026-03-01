package uz.uzumtech.jmarket.orders.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.uzumtech.common.error.core.ErrorData;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum AppError implements ErrorData {

    ERROR_ORDER_NOT_FOUND(300001, "Order not found", NOT_FOUND),
    ERROR_ORDER_INVALID_STATUS(300002, "Order cannot be updated from this status");

    private final Integer code;
    private final String message;
    private final HttpStatus status;

    AppError(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.status = BAD_REQUEST;
    }

    @Override
    public HttpStatus getStatus() {
        return status != null ? status : ErrorData.super.getStatus();
    }
}
