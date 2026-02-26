package uz.uzumtech.users.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.users.entity.UserRole;
import uz.uzumtech.users.generated.dto.MerchantCreateRequestDto;
import uz.uzumtech.users.generated.dto.MerchantProfileResponseDto;
import uz.uzumtech.users.generated.dto.MerchantStatusUpdateRequestDto;
import uz.uzumtech.users.generated.dto.MerchantVerificationStatusDto;
import uz.uzumtech.users.mapper.UserMapper;
import uz.uzumtech.users.repository.MerchantRepository;
import uz.uzumtech.users.repository.UserRepository;
import uz.uzumtech.users.service.MerchantService;
import uz.uzumtech.users.service.NotificationService;

import java.util.UUID;

import static uz.uzumtech.users.constant.AppError.ERROR_EMAIL_NOT_VERIFIED;
import static uz.uzumtech.users.constant.AppError.ERROR_MERCHANT_ALREADY_EXISTS;
import static uz.uzumtech.users.constant.AppError.ERROR_MERCHANT_ALREADY_VERIFIED;
import static uz.uzumtech.users.constant.AppError.ERROR_MERCHANT_NOT_FOUND;
import static uz.uzumtech.users.constant.AppError.ERROR_USER_NOT_FOUND;
import static uz.uzumtech.users.generated.dto.UserRoleDto.MERCHANT;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final NotificationService notificationService;
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public MerchantProfileResponseDto getMerchantProfile(UUID userId) {
        log.info("Service: getMerchantProfile for userId = {}", userId);
        return merchantRepository.findByUserId(userId)
                .map(mapper::toMerchantDto)
                .orElseThrow(() -> new CommonException(ERROR_MERCHANT_NOT_FOUND));
    }

    @Override
    @Transactional
    public MerchantProfileResponseDto onboardMerchant(UUID userId, MerchantCreateRequestDto request) {
        log.info("Service: onboardMerchant for userId = {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ERROR_USER_NOT_FOUND));

        if (!user.getIsEmailVerified()) {
            throw new CommonException(ERROR_EMAIL_NOT_VERIFIED);
        }

        if (user.getMerchantProfile() != null) {
            throw new CommonException(ERROR_MERCHANT_ALREADY_EXISTS);
        }

        var profile = mapper.toMerchantEntity(request);
        profile.setUser(user);

        var merchantRole = new UserRole();
        merchantRole.setRole(MERCHANT);
        user.addRole(merchantRole);

        merchantRepository.save(profile);

        return mapper.toMerchantDto(profile);
    }

    @Override
    @Transactional
    public void updateMerchantStatus(UUID merchantId, MerchantStatusUpdateRequestDto request) {
        log.info("Service: updateMerchantStatus for merchantId = {}", merchantId);
        var profile = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new CommonException(ERROR_MERCHANT_NOT_FOUND));

        if (profile.getVerificationStatus() == MerchantVerificationStatusDto.VERIFIED) {
            throw new CommonException(ERROR_MERCHANT_ALREADY_VERIFIED);
        }

        profile.setVerificationStatus(request.getStatus());

        merchantRepository.save(profile);

        if (request.getStatus() == MerchantVerificationStatusDto.VERIFIED) {
            log.info("Service: Send merchant verified notification for merchantId = {}", merchantId);
            notificationService.merchantVerified(profile.getId(), profile.getUser().getEmail());
        }
    }
}
