package uz.uzumtech.jmarket.integration.users.service;

import uz.uzumtech.jmarket.integration.users.dto.AddressCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.AddressResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.CustomerProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.UserResponseDto;

import java.util.UUID;

public interface UserService {

    AddressResponseDto addCustomerAddress(AddressCreateRequestDto request);

    void deleteCustomerAddress(UUID addressId);

    MerchantProfileResponseDto onboardMerchant(MerchantCreateRequestDto request);

    void setDefaultAddress(UUID addressId);

    CustomerProfileResponseDto getCustomerProfile();

    MerchantProfileResponseDto getMerchantProfile();

    UserResponseDto getMe();
}
