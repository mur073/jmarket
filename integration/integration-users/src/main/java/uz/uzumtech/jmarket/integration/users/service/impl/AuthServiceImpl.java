package uz.uzumtech.jmarket.integration.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import uz.uzumtech.common.client.users.UsersApiClient;
import uz.uzumtech.common.client.users.dto.ExRegisterCompensateRequestDto;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.common.error.exception.InternalServerErrorException;
import uz.uzumtech.jmarket.integration.users.config.property.KeycloakProperties;
import uz.uzumtech.jmarket.integration.users.dto.AuthTokenResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.LoginRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.RegisterRequestDto;
import uz.uzumtech.jmarket.integration.users.mapper.UserMapper;
import uz.uzumtech.jmarket.integration.users.service.AuthService;

import java.util.Collections;

import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;
import static uz.uzumtech.jmarket.integration.users.dto.UserRoleDto.DEFAULT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersApiClient userClient;
    private final Keycloak keycloakAdmin;
    private final RestClient restClient;
    private final UserMapper mapper;

    private final KeycloakProperties kcProps;

    @Override
    public AuthTokenResponseDto register(RegisterRequestDto request) {
        log.info("Service: register for user = {}", request.getEmail());
        var createRequest = mapper.toRegisterRequest(request);

        var result = userClient.usersRegister(createRequest);

        try {
            var kcUser = mapper.toKeycloakUser(result);
            var credential = mapper.toKeycloakCredentials(request);
            kcUser.setCredentials(Collections.singletonList(credential));
            kcUser.setRealmRoles(Collections.singletonList(DEFAULT.getValue()));

            var response = keycloakAdmin.realm(kcProps.getRealm()).users().create(kcUser);
            var kcUserId = getCreatedId(response);
            var defaultRole = keycloakAdmin.realm(kcProps.getRealm())
                    .roles()
                    .get(DEFAULT.getValue())
                    .toRepresentation();

            keycloakAdmin.realm(kcProps.getRealm())
                    .users()
                    .get(kcUserId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(defaultRole));
        } catch (Exception e) {
            log.error("Service: failed to register user = {}", request.getEmail(), e);

            userClient.usersRegisterCompensate(new ExRegisterCompensateRequestDto(result.getId()));

            throw new InternalServerErrorException("Something went wrong");
        }

        return login(new LoginRequestDto(result.getEmail(), request.getPassword()));
    }

    @Override
    public AuthTokenResponseDto login(LoginRequestDto request) {
        log.info("Service: login for user = {}", request.getEmail());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", kcProps.getClientId());
        map.add("client_secret", kcProps.getClientSecret());
        map.add("grant_type", "password");
        map.add("username", request.getEmail());
        map.add("password", request.getPassword());

        try {
            return restClient.post()
                    .uri(kcProps.getTokenEndpoint())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(map)
                    .retrieve()
                    .body(AuthTokenResponseDto.class);

        } catch (Exception e) {
            log.error("Service: login failed for user: {}", request.getEmail(), e);
            throw new CommonException(999400, "Wrong Credentials");
        }
    }
}
