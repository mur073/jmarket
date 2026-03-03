package uz.uzumtech.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzumtech.users.entity.CustomerAddress;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<CustomerAddress, UUID> {

    @Modifying
    @Query("update CustomerAddress a set a.isDefault = false where a.customer.user.id = :id")
    void unsetAllDefaults(@Param("id") UUID id);

    @Modifying
    @Query("update CustomerAddress a set a.isDefault = true where a.id = :addressId and a.customer.user.id = :id")
    int setAddressAsDefault(@Param("addressId") UUID addressId, @Param("id") UUID id);

    @Modifying
    @Query("delete CustomerAddress a where a.id = :id and a.customer.user.id = :userId")
    int deleteByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}
