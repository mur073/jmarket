package uz.uzumtech.jmarket.integration.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.stereotype.Service;
import uz.uzumtech.common.client.users.UsersApiClient;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.jmarket.integration.users.config.property.KeycloakProperties;
import uz.uzumtech.jmarket.integration.users.dto.AddressCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.AddressResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.CustomerProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.UserResponseDto;
import uz.uzumtech.jmarket.integration.users.mapper.UserMapper;
import uz.uzumtech.jmarket.integration.users.service.UserService;

import java.util.Collections;
import java.util.UUID;

import static uz.uzumtech.jmarket.integration.users.security.SecurityUtils.getCurrentUserOrThrow;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersApiClient userClient;
    private final Keycloak keycloakAdmin;
    private final UserMapper mapper;

    private final KeycloakProperties kcProps;

    @Override
    public AddressResponseDto addCustomerAddress(AddressCreateRequestDto request) {
        var userId = getCurrentUserOrThrow();
        log.info("Service: addCustomerAddress for userId = {}", userId);

        var address = userClient.addCustomerAddress(userId, mapper.toAddCustomerAddress(request));
        return mapper.toAddressResponse(address);
    }

    @Override
    public void deleteCustomerAddress(UUID addressId) {
        var userId = getCurrentUserOrThrow();
        log.info("Service: deleteCustomerAddress for userId = {}", userId);

        userClient.deleteCustomerAddress(userId, addressId);
    }

    @Override
    public MerchantProfileResponseDto onboardMerchant(MerchantCreateRequestDto request) {
        var userId = getCurrentUserOrThrow();
        log.info("Service: onboardMerchant for userId = {}", userId);

        var searchResults = keycloakAdmin.realm(kcProps.getRealm()).users().search(userId.toString(), true);
        if (searchResults.isEmpty()) {
            throw new CommonException(999400, "User not found");
        }

        var kcUserId = searchResults.getFirst().getId();
        var merchantRole = keycloakAdmin.realm(kcProps.getRealm()).roles().get("merchant").toRepresentation();

        keycloakAdmin.realm(kcProps.getRealm()).users().get(kcUserId).roles().realmLevel()
                .add(Collections.singletonList(merchantRole));

        var profile = userClient.onboardMerchant(userId, mapper.toOnboardMerchant(request));
        return mapper.toMerchantProfileResponse(profile);
    }

    @Override
    public void setDefaultAddress(UUID addressId) {
        var userId = getCurrentUserOrThrow();
        log.info("Service: setDefaultAddress for userId = {}", userId);

        userClient.setDefaultAddress(userId, addressId);
    }

    @Override
    public CustomerProfileResponseDto getCustomerProfile() {
        var userId = getCurrentUserOrThrow();
        log.info("Service: getCustomerProfile for userId = {}", userId);

        var result = userClient.usersCustomerProfileGet(userId);
        return mapper.toCustomerProfileResponse(result);
    }

    @Override
    public MerchantProfileResponseDto getMerchantProfile() {
        var userId = getCurrentUserOrThrow();
        log.info("Service: getMerchantProfile for userId = {}", userId);

        var result = userClient.usersMerchantProfileGet(userId);
        return mapper.toMerchantProfileResponse(result);
    }

    @Override
    public UserResponseDto getMe() {
        var userId = getCurrentUserOrThrow();
        log.info("Service: getMe for userId = {}", userId);

        var result = userClient.usersGetById(userId);
        return mapper.toUserResponse(result);
    }
}
