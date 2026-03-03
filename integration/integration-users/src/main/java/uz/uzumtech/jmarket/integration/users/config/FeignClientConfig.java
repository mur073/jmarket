package uz.uzumtech.jmarket.integration.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.common.error.exception.InternalServerErrorException;

import java.io.InputStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FeignClientConfig {

    private final ObjectMapper objectMapper;

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
}
