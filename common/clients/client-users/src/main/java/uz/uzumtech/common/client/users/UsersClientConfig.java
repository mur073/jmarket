package uz.uzumtech.common.client.users;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = UsersApiClient.class)
public class UsersClientConfig {
}
