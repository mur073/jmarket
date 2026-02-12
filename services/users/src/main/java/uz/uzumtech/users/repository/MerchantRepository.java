package uz.uzumtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzumtech.users.entity.MerchantProfile;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<MerchantProfile, UUID> {

    Optional<MerchantProfile> findByUserId(UUID userId);
}
