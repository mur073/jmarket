package uz.uzumtech.common.client.products;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = ProductApiClient.class)
public class ProductClientConfig {
}
