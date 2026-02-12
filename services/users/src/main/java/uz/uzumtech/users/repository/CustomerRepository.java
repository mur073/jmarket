package uz.uzumtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.users.entity.CustomerProfile;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerProfile, UUID> {

    Optional<CustomerProfile> findByUserId(UUID userId);
}
