package uz.uzumtech.common.client.users;


import org.springframework.cloud.openfeign.FeignClient;
import uz.uzumtech.common.client.users.api.UsersApi;

@FeignClient(
        name = "${app.client.users.name:users-service}",
        path = "/v1",
        url = "${app.client.users.url:http://localhost:8000}"
)
public interface UsersApiClient extends UsersApi {
}
