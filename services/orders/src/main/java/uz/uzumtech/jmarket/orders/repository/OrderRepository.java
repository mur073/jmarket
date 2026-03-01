package uz.uzumtech.jmarket.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.uzumtech.jmarket.orders.entity.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("""
            select o
            from Order o
            join fetch o.items
            where o.id = :order and o.customerId = :customer
            """)
    Optional<Order> findByIdAndCustomerId(UUID order, UUID customer);

    Page<Order> findAllByCustomerId(UUID customerId, Pageable pageable);
}
