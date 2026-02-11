package uz.uzumtech.common.error.core;

public class ErrorConstants {

    public static final Integer CODE_DEFAULT = 999400;

    public static final Integer CODE_BAD_REQUEST = 999400;
    public static final Integer CODE_UNAUTHORIZED = 999401;
    public static final Integer CODE_FORBIDDEN = 999403;
    public static final Integer CODE_NOT_FOUND = 999404;
    public static final Integer CODE_METHOD_NOT_ALLOWED = 999405;

    public static final Integer CODE_INTERNAL_SERVER_ERROR = 999500;

    private ErrorConstants() {
    }

    public static Integer getGlobalCode(Integer status) {
        return status + 999000;
    }
}
