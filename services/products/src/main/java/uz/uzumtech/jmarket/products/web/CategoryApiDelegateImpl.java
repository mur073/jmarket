package uz.uzumtech.jmarket.products.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.uzumtech.jmarket.products.generated.api.CategoriesApiDelegate;
import uz.uzumtech.jmarket.products.generated.dto.CategoryPageResponseDto;
import uz.uzumtech.jmarket.products.service.CategoryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryApiDelegateImpl implements CategoriesApiDelegate {

    private final CategoryService service;

    @Override
    public ResponseEntity<CategoryPageResponseDto> categoriesGet(Integer page, Integer size) {
        log.info("REST: categoriesGet request");
        return ResponseEntity.ok(service.getCategories(page, size));
    }
}
