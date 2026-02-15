package uz.uzumtech.jmarket.products.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.jmarket.products.entity.Product;
import uz.uzumtech.jmarket.products.generated.dto.ProductCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductUpdateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewPageResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewResponseDto;
import uz.uzumtech.jmarket.products.mapper.ProductMapper;
import uz.uzumtech.jmarket.products.repository.CategoryRepository;
import uz.uzumtech.jmarket.products.repository.ProductRepository;
import uz.uzumtech.jmarket.products.repository.ReviewRepository;
import uz.uzumtech.jmarket.products.service.ProductService;

import java.util.UUID;

import static ch.qos.logback.core.util.StringUtil.notNullNorEmpty;
import static java.util.Objects.nonNull;
import static uz.uzumtech.jmarket.products.constant.AppError.ERROR_INVALID_CATEGORY;
import static uz.uzumtech.jmarket.products.constant.AppError.ERROR_PRODUCT_NOT_FOUND;
import static uz.uzumtech.jmarket.products.constant.AppError.ERROR_REVIEW_EXISTS;
import static uz.uzumtech.jmarket.products.security.SecurityUtils.getCurrentUserOrThrow;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto request) {
        var currentUser = getCurrentUserOrThrow();
        log.info("Service: createProduct userId = {}", currentUser);

        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CommonException(ERROR_INVALID_CATEGORY));

        var product = mapper.toProductEntity(request);

        product.setMerchantId(currentUser);
        product.setCategory(category);

        return mapper.toProductDto(productRepository.save(product));
    }

    @Override
    public ReviewPageResponseDto getProductReviews(UUID productId, Integer page, Integer size) {
        log.info("Service: getProductReviews for productId = {}", productId);

        var pageRequest = PageRequest.of(page, size);
        var reviews = reviewRepository.findByProduct_Id(productId, pageRequest);

        return ReviewPageResponseDto.builder()
                .page(page)
                .pageSize(size)
                .totalItems((int) reviews.getTotalElements())
                .totalPages(reviews.getTotalPages())
                .items(reviews.get().map(mapper::toReviewDto).toList())
                .build();
    }

    @Override
    @Transactional
    public void updateProduct(UUID productId, ProductUpdateRequestDto request) {
        var currentUser = getCurrentUserOrThrow();
        log.info("Service: updateProduct for productId = {} by userId = {}", productId, currentUser);

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new CommonException(ERROR_PRODUCT_NOT_FOUND));

        if (notNullNorEmpty(request.getTitle())) {
            product.setTitle(request.getTitle());
        }

        if (notNullNorEmpty(request.getDescription())) {
            product.setDescription(request.getDescription());
        }

        if (nonNull(request.getPrice())) {
            product.setPrice(mapper.mapPrice(request.getPrice()));
        }

        if (nonNull(request.getQuantityInStock())) {
            product.setQuantityInStock(request.getQuantityInStock());
        }

        if (nonNull(request.getIsActive())) {
            product.setIsActive(request.getIsActive());
        }
    }

    @Override
    public ProductPageResponseDto getProducts(UUID categoryId, UUID merchantId, Integer page, Integer size) {
        log.info("Service: getProducts by categoryId = {} and merchantId = {}", categoryId, merchantId);
        var pageRequest = PageRequest.of(page, size);
        var products = productRepository
                .findAll(getProductsSpecs(categoryId, merchantId), pageRequest);

        return ProductPageResponseDto.builder()
                .page(page)
                .pageSize(size)
                .totalItems((int) products.getTotalElements())
                .totalPages(products.getTotalPages())
                .items(products.get().map(mapper::toProductDto).toList())
                .build();
    }

    @Override
    public ProductResponseDto getProductById(UUID productId) {
        log.info("Service: getProductById for productId = {}", productId);
        return productRepository.findById(productId)
                .map(mapper::toProductDto)
                .orElseThrow(() -> new CommonException(ERROR_PRODUCT_NOT_FOUND));
    }

    @Override
    @Transactional
    public ReviewResponseDto createProductReview(UUID productId, ReviewCreateRequestDto request) {
        log.info("Service: createProductReview for productId = {}", productId);
        var currentUser = getCurrentUserOrThrow();
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new CommonException(ERROR_PRODUCT_NOT_FOUND));

        var existing = reviewRepository.findByCustomerIdAndProductId(currentUser, productId);
        if (existing.isPresent()) {
            throw new CommonException(ERROR_REVIEW_EXISTS);
        }

        var rating = product.getAverageRating() * product.getReviewCount() + request.getRating();
        var reviewsCount = product.getReviewCount() + 1;

        product.setReviewCount(reviewsCount);
        product.setAverageRating(rating / reviewsCount);

        var review = mapper.toReviewEntity(request);
        review.setCustomerId(currentUser);
        review.setProduct(product);

        return mapper.toReviewDto(reviewRepository.save(review));
    }

    private Specification<Product> getProductsSpecs(UUID categoryId, UUID merchantId) {
        return (root, query, cb) -> {
            if (categoryId != null && merchantId != null) {
                return cb.and(
                        cb.equal(root.get("category").get("id"), categoryId),
                        cb.equal(root.get("merchantId"), merchantId)
                );
            }

            if (categoryId != null) {
                return cb.equal(root.get("category").get("id"), categoryId);
            }

            if (merchantId != null) {
                return cb.equal(root.get("merchantId"), merchantId);
            }

            return cb.conjunction();
        };
    }
}
