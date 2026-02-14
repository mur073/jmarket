package uz.uzumtech.jmarket.products.service;

import uz.uzumtech.jmarket.products.generated.dto.ProductCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductUpdateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewResponseDto;

import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductCreateRequestDto request);

    ReviewPageResponseDto getProductReviews(UUID productId, Integer page, Integer size);

    void updateProduct(UUID productId, ProductUpdateRequestDto request);

    ProductPageResponseDto getProducts(UUID categoryId, UUID merchantId, Integer page, Integer size);

    ProductResponseDto getProductById(UUID productId);

    ReviewResponseDto createProductReview(UUID productId, ReviewCreateRequestDto request);
}
