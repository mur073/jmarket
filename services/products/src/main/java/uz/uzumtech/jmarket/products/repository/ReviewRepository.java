package uz.uzumtech.jmarket.products.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.jmarket.products.entity.Review;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByProduct_Id(UUID productId, Pageable pageable);

    @Query("""
                select r
                from Review r
                where r.customerId = :customer and r.product.id = :product
            """)
    Optional<Review> findByCustomerIdAndProductId(@Param("customer") UUID customerId, @Param("product") UUID productId);
}
