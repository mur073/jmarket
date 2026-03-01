package uz.uzumtech.jmarket.orders.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import uz.uzumtech.common.error.exception.UnauthorizedException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
    }

    public static UUID getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
    }

    public static Optional<UUID> getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                return Optional.ofNullable(jwt.getClaimAsString("preferred_username"))
                        .map(UUID::fromString);
            }
        } catch (Exception e) {
            log.error("SecurityUtils: unauthorized error: {}", e.getMessage());
        }

        throw new UnauthorizedException("Unauthorized");
    }
}
