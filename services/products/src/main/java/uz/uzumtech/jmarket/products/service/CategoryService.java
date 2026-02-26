package uz.uzumtech.jmarket.products.service;

import uz.uzumtech.jmarket.products.generated.dto.CategoryPageResponseDto;

public interface CategoryService {

    CategoryPageResponseDto getCategories(int page, int size);
}
