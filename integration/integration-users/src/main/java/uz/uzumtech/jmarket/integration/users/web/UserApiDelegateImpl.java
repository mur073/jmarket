package uz.uzumtech.jmarket.integration.users.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.uzumtech.jmarket.integration.users.api.UsersApiDelegate;
import uz.uzumtech.jmarket.integration.users.dto.AddressCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.AddressResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.AuthTokenResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.CustomerProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.LoginRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.RegisterRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.UserResponseDto;
import uz.uzumtech.jmarket.integration.users.service.AuthService;
import uz.uzumtech.jmarket.integration.users.service.UserService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UsersApiDelegate {

    private final UserService userService;
    private final AuthService authService;

    @Override
    public ResponseEntity<AddressResponseDto> addCustomerAddress(AddressCreateRequestDto request) {
        return ok(userService.addCustomerAddress(request));
    }

    @Override
    public ResponseEntity<Void> deleteCustomerAddress(UUID addressId) {
        userService.deleteCustomerAddress(addressId);
        return ok().build();
    }

    @Override
    public ResponseEntity<MerchantProfileResponseDto> onboardMerchant(MerchantCreateRequestDto request) {
        return ok(userService.onboardMerchant(request));
    }

    @Override
    public ResponseEntity<Void> setDefaultAddress(UUID addressId) {
        userService.setDefaultAddress(addressId);
        return ok().build();
    }

    @Override
    public ResponseEntity<CustomerProfileResponseDto> usersCustomerProfileGet() {
        return ok(userService.getCustomerProfile());
    }

    @Override
    public ResponseEntity<UserResponseDto> usersGetMe() {
        return ok(userService.getMe());
    }

    @Override
    public ResponseEntity<MerchantProfileResponseDto> usersMerchantProfileGet() {
        return ok(userService.getMerchantProfile());
    }

    @Override
    public ResponseEntity<AuthTokenResponseDto> usersLogin(LoginRequestDto request) {
        return ok(authService.login(request));
    }

    @Override
    public ResponseEntity<AuthTokenResponseDto> usersRegister(RegisterRequestDto request) {
        return ok(authService.register(request));
    }
}
