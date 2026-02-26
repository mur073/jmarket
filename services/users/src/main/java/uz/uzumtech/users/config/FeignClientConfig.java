package uz.uzumtech.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.common.error.exception.InternalServerErrorException;
import uz.uzumtech.users.config.property.NotificationProperties;

import java.io.InputStream;
import java.util.Base64;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    private final NotificationProperties notificationProperties;

    private final ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor basicAuthInterceptor() {
        return requestTemplate -> {
            String clientName = requestTemplate.feignTarget().name();

            if (notificationProperties.getName().equals(clientName)) {
                requestTemplate.header(AUTHORIZATION, "Basic " + encode(
                        notificationProperties.getUser(),
                        notificationProperties.getPassword()
                ));
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
                        log.error("Unexpected response from = {} with status = {}", methodKey, response.status());
                        return new InternalServerErrorException("Internal Server Error");
                    }

                default:
                    log.error("Unexpected response from = {} with status = {}", methodKey, response.status());
                    return new InternalServerErrorException("Internal Server Error");
            }
        };
    }

    private String encode(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
