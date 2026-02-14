package uz.uzumtech.jmarket.products.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uz.uzumtech.common.error.exception.UnauthorizedException;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<UUID> getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractCurrentUser(context.getAuthentication()));
    }

    public static UUID getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));
    }

    private static UUID extractCurrentUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwt)) {
            return null;
        }

        return UUID.fromString(jwt.getToken().getClaim("sub"));
    }
}
