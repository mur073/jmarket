package uz.uzumtech.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.users.entity.CustomerAddress;
import uz.uzumtech.users.generated.dto.AddressCreateRequestDto;
import uz.uzumtech.users.generated.dto.AddressResponseDto;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    CustomerAddress toAddressEntity(AddressCreateRequestDto request);

    AddressResponseDto toAddressDto(CustomerAddress save);
}
