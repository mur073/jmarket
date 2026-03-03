package uz.uzumtech.jmarket.integration.users.mapper;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.common.client.users.dto.ExAddressCreateRequestDto;
import uz.uzumtech.common.client.users.dto.ExAddressResponseDto;
import uz.uzumtech.common.client.users.dto.ExCustomerProfileResponseDto;
import uz.uzumtech.common.client.users.dto.ExMerchantCreateRequestDto;
import uz.uzumtech.common.client.users.dto.ExMerchantProfileResponseDto;
import uz.uzumtech.common.client.users.dto.ExRegisterRequestDto;
import uz.uzumtech.common.client.users.dto.ExUserResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.AddressCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.AddressResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.CustomerProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantCreateRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.MerchantProfileResponseDto;
import uz.uzumtech.jmarket.integration.users.dto.RegisterRequestDto;
import uz.uzumtech.jmarket.integration.users.dto.UserResponseDto;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface UserMapper {

    ExAddressCreateRequestDto toAddCustomerAddress(AddressCreateRequestDto request);

    AddressResponseDto toAddressResponse(ExAddressResponseDto address);

    ExMerchantCreateRequestDto toOnboardMerchant(MerchantCreateRequestDto request);

    MerchantProfileResponseDto toMerchantProfileResponse(ExMerchantProfileResponseDto profile);

    CustomerProfileResponseDto toCustomerProfileResponse(ExCustomerProfileResponseDto result);

    UserResponseDto toUserResponse(ExUserResponseDto result);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "enabled", constant = "true")
    UserRepresentation toKeycloakUser(ExUserResponseDto user);

    ExRegisterRequestDto toRegisterRequest(RegisterRequestDto request);

    @Mapping(target = "type", constant = "PASSWORD")
    @Mapping(target = "value", source = "request.password")
    @Mapping(target = "temporary", constant = "false")
    CredentialRepresentation toKeycloakCredentials(RegisterRequestDto request);

    default String mapUUIDtoString(UUID uuid) {
        return String.valueOf(uuid);
    }
}
