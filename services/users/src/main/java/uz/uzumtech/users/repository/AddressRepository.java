package uz.uzumtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.uzumtech.users.entity.CustomerAddress;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<CustomerAddress, UUID> {

    Optional<CustomerAddress> findByIdAndCustomerId(UUID id, UUID customerId);

    @Modifying
    @Query("UPDATE CustomerAddress a SET a.isDefault = false WHERE a.customer.id = :customerId")
    void unsetAllDefaultsForCustomer(UUID customerId);

    @Modifying
    @Query("UPDATE CustomerAddress a SET a.isDefault = true WHERE a.id = :addressId AND a.customer.id = :customerId")
    int setAddressAsDefault(UUID addressId, UUID customerId);
}
