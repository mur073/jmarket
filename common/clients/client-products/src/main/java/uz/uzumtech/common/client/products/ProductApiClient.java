package uz.uzumtech.common.client.products;

import org.springframework.cloud.openfeign.FeignClient;
import uz.uzumtech.common.client.products.api.ProductsApi;

@FeignClient(
        name = "${app.client.products.name:products-service}",
        path = "/api/v1",
        url = "${app.client.products.url:http://localhost:8000}"
)
public interface ProductApiClient extends ProductsApi {
}
