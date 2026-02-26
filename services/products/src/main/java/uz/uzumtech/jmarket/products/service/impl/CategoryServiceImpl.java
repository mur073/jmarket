package uz.uzumtech.jmarket.products.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.uzumtech.jmarket.products.generated.dto.CategoryPageResponseDto;
import uz.uzumtech.jmarket.products.mapper.CategoryMapper;
import uz.uzumtech.jmarket.products.repository.CategoryRepository;
import uz.uzumtech.jmarket.products.service.CategoryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryPageResponseDto getCategories(int page, int size) {
        var pageRequest = PageRequest.of(page, size);
        var categories = repository.findAll(pageRequest);

        return CategoryPageResponseDto.builder()
                .page(page)
                .pageSize(size)
                .totalItems((int) categories.getTotalElements())
                .totalPages(categories.getTotalPages())
                .items(categories.get().map(mapper::toCategoryDto).toList())
                .build();
    }
}
