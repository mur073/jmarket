package uz.uzumtech.jmarket.products.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.jmarket.products.entity.Category;
import uz.uzumtech.jmarket.products.generated.dto.CategoryResponseDto;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface CategoryMapper {

    CategoryResponseDto toCategoryDto(Category category);
}
