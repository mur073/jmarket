package uz.uzumtech.users.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import uz.uzumtech.common.error.core.ErrorData;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum AppError implements ErrorData {

    ERROR_EMAIL_TAKEN(100000, "Email is taken"),
    ERROR_EMAIL_NOT_VERIFIED(100001, "Email must be verified"),
    ERROR_USER_NOT_FOUND(100005, "User not found", NOT_FOUND),
    ERROR_MERCHANT_NOT_FOUND(100006, "Merchant not found", NOT_FOUND),
    ERROR_CUSTOMER_NOT_FOUND(100007, "Customer not found", NOT_FOUND),
    ERROR_MERCHANT_ALREADY_EXISTS(100008, "Merchant already exists"),
    ERROR_ADDRESS_LIMIT_EXCEEDED(100009, "Address limit exceeded"),
    ERROR_ADDRESS_NOT_FOUND(100010, "Address not found", NOT_FOUND),
    ERROR_MERCHANT_ALREADY_VERIFIED(100011, "Merchant already verified");

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
