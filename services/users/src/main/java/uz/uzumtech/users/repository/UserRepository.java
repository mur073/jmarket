package uz.uzumtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.users.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    Optional<User> findById(UUID uuid);

    Optional<User> findByEmail(String email);

    @Query("""
                    select u
                    from User u
                    where u.customerProfile.id = :profileId
            """)
    Optional<User> findByCustomerProfileId(@Param("profileId") UUID profileId);

    @Query("""
                    select u
                    from User u
                    where u.merchantProfile.id = :profileId
            """)
    Optional<User> findByMerchantProfileId(@Param("profileId") UUID profileId);
}
