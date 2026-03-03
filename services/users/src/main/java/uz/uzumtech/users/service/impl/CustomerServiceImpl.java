package uz.uzumtech.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.users.generated.dto.AddressCreateRequestDto;
import uz.uzumtech.users.generated.dto.AddressResponseDto;
import uz.uzumtech.users.generated.dto.CustomerProfileResponseDto;
import uz.uzumtech.users.mapper.AddressMapper;
import uz.uzumtech.users.mapper.UserMapper;
import uz.uzumtech.users.repository.AddressRepository;
import uz.uzumtech.users.repository.CustomerRepository;
import uz.uzumtech.users.service.CustomerService;

import java.util.UUID;

import static java.lang.Boolean.TRUE;
import static uz.uzumtech.users.constant.AppError.ERROR_ADDRESS_LIMIT_EXCEEDED;
import static uz.uzumtech.users.constant.AppError.ERROR_CUSTOMER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final int MAX_ADDRESSES = 5;

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;

    @Override
    public CustomerProfileResponseDto getCustomerProfile(UUID userId) {
        log.info("Service: getCustomerProfile for userId = {}", userId);
        return customerRepository.findByUserId(userId)
                .map(userMapper::toCustomerDto)
                .orElseThrow(() -> new CommonException(ERROR_CUSTOMER_NOT_FOUND));
    }

    @Override
    @Transactional
    public AddressResponseDto addCustomerAddress(UUID userId, AddressCreateRequestDto request) {
        log.info("Service: addCustomerAddress for userId = {}", userId);
        var customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ERROR_CUSTOMER_NOT_FOUND));

        if (customer.getAddresses().size() >= MAX_ADDRESSES) {
            throw new CommonException(ERROR_ADDRESS_LIMIT_EXCEEDED);
        }

        var newAddress = addressMapper.toAddressEntity(request);


        if (TRUE.equals(request.getIsDefault()) || customer.getAddresses().isEmpty()) {
            newAddress.setIsDefault(true);
            addressRepository.unsetAllDefaults(userId);
        }

        newAddress.setCustomer(customer);
        customer.getAddresses().add(newAddress);

        return addressMapper.toAddressDto(addressRepository.save(newAddress));
    }

    @Override
    @Transactional
    public void deleteCustomerAddress(UUID userId, UUID addressId) {
        log.info("Service: deleteCustomerAddress for userId = {}", userId);
        addressRepository.deleteByIdAndUserId(addressId, userId);
    }

    @Override
    @Transactional
    public void setDefaultAddress(UUID userId, UUID addressId) {
        log.info("Service: setDefaultAddress for userId = {}", userId);
        addressRepository.unsetAllDefaults(userId);
        addressRepository.setAddressAsDefault(addressId, userId);
    }
}
