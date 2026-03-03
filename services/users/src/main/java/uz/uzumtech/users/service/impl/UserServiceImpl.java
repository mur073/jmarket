package uz.uzumtech.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.users.generated.dto.ProfileIdTypeDto;
import uz.uzumtech.users.generated.dto.RegisterCompensateRequestDto;
import uz.uzumtech.users.generated.dto.RegisterRequestDto;
import uz.uzumtech.users.generated.dto.UserResponseDto;
import uz.uzumtech.users.mapper.UserMapper;
import uz.uzumtech.users.repository.UserRepository;
import uz.uzumtech.users.service.UserService;

import java.util.UUID;

import static uz.uzumtech.users.constant.AppError.ERROR_EMAIL_TAKEN;
import static uz.uzumtech.users.constant.AppError.ERROR_USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public UserResponseDto getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(mapper::toUserDto)
                .orElseThrow(() -> new CommonException(ERROR_USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public UserResponseDto register(RegisterRequestDto request) {
        log.info("Service: register user with email = {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CommonException(ERROR_EMAIL_TAKEN);
        }

        var user = mapper.toUserEntity(request);

        return mapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getUserByProfileId(ProfileIdTypeDto profileIdType, UUID profileId) {
        log.info("Service: getUserByProfileId: ProfileType = [{}], ProfileId = [{}]", profileIdType, profileId);

        var user = switch (profileIdType) {
            case CUSTOMER -> userRepository.findByCustomerProfileId(profileId);
            case MERCHANT -> userRepository.findByMerchantProfileId(profileId);
        };

        return user.map(mapper::toUserDto)
                .orElseThrow(() -> new CommonException(ERROR_USER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void cancelRegister(RegisterCompensateRequestDto request) {
        log.info("Service: cancelRegister: ", request.getUserId());

        userRepository.deleteById(request.getUserId());
    }
}
