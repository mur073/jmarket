package uz.uzumtech.users.service;

import uz.uzumtech.users.generated.dto.AddressCreateRequestDto;
import uz.uzumtech.users.generated.dto.AddressResponseDto;
import uz.uzumtech.users.generated.dto.CustomerProfileResponseDto;

import java.util.UUID;

public interface CustomerService {

    CustomerProfileResponseDto getCustomerProfile(UUID userId);

    AddressResponseDto addCustomerAddress(UUID userId, AddressCreateRequestDto request);

    void deleteCustomerAddress(UUID userId, UUID addressId);

    void setDefaultAddress(UUID userId, UUID addressId);
}
