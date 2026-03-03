package uz.uzumtech.users.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.uzumtech.users.generated.api.UsersApiDelegate;
import uz.uzumtech.users.generated.dto.AddressCreateRequestDto;
import uz.uzumtech.users.generated.dto.AddressResponseDto;
import uz.uzumtech.users.generated.dto.CustomerProfileResponseDto;
import uz.uzumtech.users.generated.dto.MerchantCreateRequestDto;
import uz.uzumtech.users.generated.dto.MerchantProfileResponseDto;
import uz.uzumtech.users.generated.dto.MerchantStatusUpdateRequestDto;
import uz.uzumtech.users.generated.dto.ProfileIdTypeDto;
import uz.uzumtech.users.generated.dto.RegisterCompensateRequestDto;
import uz.uzumtech.users.generated.dto.RegisterRequestDto;
import uz.uzumtech.users.generated.dto.UserResponseDto;
import uz.uzumtech.users.service.CustomerService;
import uz.uzumtech.users.service.MerchantService;
import uz.uzumtech.users.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UsersApiDelegate {

    private final UserService userService;
    private final CustomerService customerService;
    private final MerchantService merchantService;

    @Override
    public ResponseEntity<UserResponseDto> usersGetById(UUID userId) {
        log.info("REST: usersGetById request for userId = {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Override
    public ResponseEntity<UserResponseDto> usersRegister(RegisterRequestDto request) {
        log.info("REST: usersRegister request for email = {}", request.getEmail());
        return ResponseEntity.ok(userService.register(request));
    }

    @Override
    public ResponseEntity<CustomerProfileResponseDto> usersCustomerProfileGet(UUID userId) {
        log.info("REST: usersCustomerProfileGet request for userId = {}", userId);
        return ResponseEntity.ok(customerService.getCustomerProfile(userId));
    }

    @Override
    public ResponseEntity<MerchantProfileResponseDto> onboardMerchant(UUID userId, MerchantCreateRequestDto request) {
        log.info("REST: onboardMerchant request for userId = {}", userId);
        return ResponseEntity.ok(merchantService.onboardMerchant(userId, request));
    }

    @Override
    public ResponseEntity<MerchantProfileResponseDto> usersMerchantProfileGet(UUID userId) {
        log.info("REST: usersMerchantProfileGet request for userId = {}", userId);
        return ResponseEntity.ok(merchantService.getMerchantProfile(userId));
    }

    @Override
    public ResponseEntity<UserResponseDto> usersGetByProfileId(ProfileIdTypeDto profileIdType, UUID profileId) {
        log.info("REST: usersGetByProfileId. ProfileIdType = [{}], ProfileId = [{}]", profileIdType, profileId);
        return ResponseEntity.ok(userService.getUserByProfileId(profileIdType, profileId));
    }

    @Override
    public ResponseEntity<AddressResponseDto> addCustomerAddress(UUID userId, AddressCreateRequestDto request) {
        log.info("REST: addCustomerAddress userId = {}", userId);
        return ResponseEntity.ok(customerService.addCustomerAddress(userId, request));
    }

    @Override
    public ResponseEntity<Void> deleteCustomerAddress(UUID userId, UUID addressId) {
        log.info("REST: deleteCustomerAddress: UserId = [{}], AddressId = [{}]", userId, addressId);
        customerService.deleteCustomerAddress(userId, addressId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> setDefaultAddress(UUID userId, UUID addressId) {
        log.info("REST: setDefaultAddress: UserId = [{}], AddressId = [{}]", userId, addressId);
        customerService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> usersRegisterCompensate(RegisterCompensateRequestDto request) {
        log.info("REST: usersRegisterCompensate for user = {}", request.getUserId());
        userService.cancelRegister(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateMerchantStatus(UUID merchantId, MerchantStatusUpdateRequestDto request) {
        log.info("REST: updateMerchantStatus: UserId = [{}], Status = [{}]", merchantId, request.getStatus());
        merchantService.updateMerchantStatus(merchantId, request);
        return ResponseEntity.ok().build();
    }
}
