package uz.uzumtech.jmarket.integration.users.service;

import uz.uzumtech.jmarket.integration.users.dto.AuthTokenResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.LoginRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.RegisterRequestDto;

public interface AuthService {

    AuthTokenResponseDto register(RegisterRequestDto request);

    AuthTokenResponseDto login(LoginRequestDto request);
}
