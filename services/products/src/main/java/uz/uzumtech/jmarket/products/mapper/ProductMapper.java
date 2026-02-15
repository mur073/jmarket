package uz.uzumtech.jmarket.products.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.jmarket.products.entity.Category;
import uz.uzumtech.jmarket.products.entity.Product;
import uz.uzumtech.jmarket.products.entity.Review;
import uz.uzumtech.jmarket.products.generated.dto.CategoryResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ProductResponseDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewCreateRequestDto;
import uz.uzumtech.jmarket.products.generated.dto.ReviewResponseDto;

import java.time.Instant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface ProductMapper {

    CategoryResponseDto toCategoryDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "merchantId", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "averageRating", constant = "0.0")
    @Mapping(target = "reviewCount", constant = "0")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    Product toProductEntity(ProductCreateRequestDto request);

    @Mapping(target = "price", source = "price", qualifiedByName = "mapPrice")
    ProductResponseDto toProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    Review toReviewEntity(ReviewCreateRequestDto request);

    @Mapping(target = "productId", source = "product.id")
    ReviewResponseDto toReviewDto(Review review);

    @Named("mapPrice")
    default long mapPrice(Double price) {
        return (long) (price * 100);
    }

    @Named("mapPrice")
    default double mapPrice(Long price) {
        return price / 100.0;
    }

    default Long instantToLong(Instant date) {
        if (date == null) {
            return null;
        }
        return date.getEpochSecond();
    }
}
