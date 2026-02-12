package uz.uzumtech.users.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.users.entity.CustomerProfile;
import uz.uzumtech.users.entity.MerchantProfile;
import uz.uzumtech.users.entity.User;
import uz.uzumtech.users.entity.UserRole;
import uz.uzumtech.users.generated.dto.CustomerProfileResponseDto;
import uz.uzumtech.users.generated.dto.MerchantCreateRequestDto;
import uz.uzumtech.users.generated.dto.MerchantProfileResponseDto;
import uz.uzumtech.users.generated.dto.RegisterRequestDto;
import uz.uzumtech.users.generated.dto.UserResponseDto;
import uz.uzumtech.users.generated.dto.UserRoleDto;

import java.time.Instant;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        uses = {AddressMapper.class}
)
public interface UserMapper {

    @Mapping(target = "registeredAt", source = "createdAt")
    UserResponseDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "customerProfile", ignore = true)
    @Mapping(target = "merchantProfile", ignore = true)
    @Mapping(target = "isEmailVerified", constant = "false")
    User toUserEntity(RegisterRequestDto request);

    @Mapping(target = "userId", source = "profile.user.id")
    CustomerProfileResponseDto toCustomerDto(CustomerProfile profile);


    default UserRoleDto toUserRoleDto(UserRole userRole) {
        if (userRole == null) {
            return null;
        }
        return userRole.getRole();
    }

    @AfterMapping
    default void setDefaults(@MappingTarget User user, RegisterRequestDto request) {
        UserRole role = new UserRole();
        role.setRole(UserRoleDto.DEFAULT);
        user.addRole(role);

        CustomerProfile profile = new CustomerProfile();
        profile.setUser(user);
    }

    default Long instantToLong(Instant date) {
        if (date == null) {
            return null;
        }
        return date.getEpochSecond();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "verificationStatus", constant = "SUBMITTED")
    @Mapping(target = "rating", constant = "0.0")
    MerchantProfile toMerchantEntity(MerchantCreateRequestDto dto);

    @Mapping(target = "userId", source = "user.id")
    MerchantProfileResponseDto toMerchantDto(MerchantProfile entity);
}
