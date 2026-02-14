package uz.uzumtech.jmarket.products.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.uzumtech.common.error.core.ErrorData;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum AppError implements ErrorData {

    ERROR_INVALID_CATEGORY(200000, "Invalid category"),
    ERROR_PRODUCT_NOT_FOUND(200001, "Product not found");

    private final Integer code;
    private final String message;
    private final HttpStatus status;

    AppError(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.status = BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status != null ? status : ErrorData.super.getStatus();
    }
}
