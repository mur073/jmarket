package uz.uzumtech.users.service;

import uz.uzumtech.users.generated.dto.MerchantCreateRequestDto;
import uz.uzumtech.users.generated.dto.MerchantProfileResponseDto;
import uz.uzumtech.users.generated.dto.MerchantStatusUpdateRequestDto;

import java.util.UUID;

public interface MerchantService {

    MerchantProfileResponseDto getMerchantProfile(UUID userId);

    MerchantProfileResponseDto onboardMerchant(UUID userId, MerchantCreateRequestDto request);

    void updateMerchantStatus(UUID merchantId, MerchantStatusUpdateRequestDto request);
}
