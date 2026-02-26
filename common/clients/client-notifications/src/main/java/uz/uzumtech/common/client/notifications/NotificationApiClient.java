package uz.uzumtech.common.client.notifications;

import org.springframework.cloud.openfeign.FeignClient;
import uz.uzumtech.common.client.notifications.api.NotificationsApi;

@FeignClient(
        name = "${app.client.notifications.name:notifications-service}",
        path = "/api",
        url = "${app.client.notifications.url:http://localhost:8000}"
)
public interface NotificationApiClient extends NotificationsApi {
}
