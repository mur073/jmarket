package uz.uzumtech.users.service;

import uz.uzumtech.users.generated.dto.ProfileIdTypeDto;
import uz.uzumtech.users.generated.dto.RegisterRequestDto;
import uz.uzumtech.users.generated.dto.UserResponseDto;

import java.util.UUID;

public interface UserService {

    UserResponseDto getUserById(UUID userId);

    UserResponseDto register(RegisterRequestDto request);

    UserResponseDto getUserByProfileId(ProfileIdTypeDto profileIdType, UUID profileId);
}
