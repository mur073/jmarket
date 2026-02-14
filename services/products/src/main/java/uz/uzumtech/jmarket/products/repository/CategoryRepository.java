package uz.uzumtech.jmarket.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.jmarket.products.entity.Category;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
