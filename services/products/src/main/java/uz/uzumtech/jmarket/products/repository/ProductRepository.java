package uz.uzumtech.jmarket.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.jmarket.products.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("""
            select p
            from Product p
            where p.id = :product and p.merchantId = :merchant
            """)
    Optional<Product> findByIdAndMerchantId(@Param("product") UUID productId, @Param("merchant") UUID merchantId);
}
