package uz.uzumtech.common.client.notifications;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = NotificationApiClient.class)
public class NotificationClientConfig {
}