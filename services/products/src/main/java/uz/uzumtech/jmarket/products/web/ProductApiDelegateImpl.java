package uz.uzumtech.jmarket.products.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.uzumtech.jmarket.products.generated.api.ProductsApiDelegate;
import uz.uzumtech.jmarket.products.generated.dto.ProductCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductReserveRequestInnerDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductUpdateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewResponseDto;
import uz.uzumtech.jmarket.products.service.ProductService;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApiDelegateImpl implements ProductsApiDelegate {

    private final ProductService service;

    @Override
    @PreAuthorize("hasRole('merchant')")
    public ResponseEntity<ProductResponseDto> createProduct(ProductCreateRequestDto request) {
        log.info("REST: createProduct request");
        return ok(service.createProduct(request));
    }

    @Override
    public ResponseEntity<ReviewPageResponseDto> productReviewsGet(UUID productId, Integer page, Integer size) {
        log.info("REST: productReviewsGet request for productId = {}, page = {}, size = {}", productId, page, size);
        return ok(service.getProductReviews(productId, page, size));
    }

    @Override
    @PreAuthorize("hasRole('merchant')")
    public ResponseEntity<Void> productUpdate(UUID productId, ProductUpdateRequestDto request) {
        log.info("REST: productUpdate request for productId = {}", productId);
        service.updateProduct(productId, request);
        return ok().build();
    }

    @Override
    public ResponseEntity<ProductPageResponseDto> productsGet(UUID categoryId, UUID merchantId, Integer page, Integer size) {
        log.info("REST: productsGet request for categoryId = {}, merchantId = {}", categoryId, merchantId);
        return ok(service.getProducts(categoryId, merchantId, page, size));
    }

    @Override
    public ResponseEntity<ProductResponseDto> productsGetById(UUID productId) {
        log.info("REST: productsGetById request for productId = {}", productId);
        return ok(service.getProductById(productId));
    }

    @Override
    public ResponseEntity<ReviewResponseDto> reviewCreate(UUID productId, ReviewCreateRequestDto request) {
        log.info("REST: reviewCreate request for productId = {}", productId);
        return ok(service.createProductReview(productId, request));
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> productsReservePost(List<@Valid ProductReserveRequestInnerDto> products) {
        log.info("REST: productsReservePost request");
        return ok(service.reserveProducts(products));
    }
}
