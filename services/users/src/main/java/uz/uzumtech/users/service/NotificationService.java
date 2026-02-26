package uz.uzumtech.users.service;

import java.util.UUID;

public interface NotificationService {

    void merchantVerified(UUID profileId, String email);
}
