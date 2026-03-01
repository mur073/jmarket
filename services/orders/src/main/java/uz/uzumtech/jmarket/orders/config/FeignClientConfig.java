package uz.uzumtech.jmarket.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.common.error.exception.InternalServerErrorException;

import java.io.InputStream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
                String token = jwtAuthenticationToken.getToken().getTokenValue();

                log.debug("FeignClient: Injecting JWT to request: {}", token);

                requestTemplate.header(AUTHORIZATION, "Bearer " + token);
            }
        };
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            switch (response.status()) {
                case 400:
                case 401:
                case 403:
                case 404:
                case 405:
                case 500:
                    try (InputStream bodyIs = response.body().asInputStream()) {
                        return objectMapper.readValue(bodyIs, CommonException.class);
                    } catch (Exception e) {
                        log.error("Unexpected response from = {} with status = {}", methodKey, response.status(), e);
                        return new InternalServerErrorException("Internal Server Error");
                    }

                default:
                    log.error("Unexpected response from = {} with status = {}", methodKey, response.status());
                    return new InternalServerErrorException("Internal Server Error");
            }
        };
    }
}
