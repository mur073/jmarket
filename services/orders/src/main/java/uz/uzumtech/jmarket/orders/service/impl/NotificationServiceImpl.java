package uz.uzumtech.jmarket.orders.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.uzumtech.common.client.notifications.NotificationApiClient;
import uz.uzumtech.common.client.notifications.dto.ExNotificationSendRequestDto;
import uz.uzumtech.common.client.notifications.dto.ExReceiverDtoDto;
import uz.uzumtech.jmarket.orders.entity.Order;
import uz.uzumtech.jmarket.orders.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Value("${app.data.notifications.text.order-created}")
    private String orderCreatedTemplate;

    private final NotificationApiClient notificationClient;

    @Override
    public void orderCreated(String email, Order order) {
        log.info("Service: send orderCreated notification to email = {}", email);
        var text = String.format(orderCreatedTemplate, order.getId().toString(), order.getTotalAmount() / 100.0);
        send(email, text);
    }

    public void send(String email, String text) {
        var receiver = ExReceiverDtoDto.builder()
                .email(email).phone("998000000000").firebaseToken("token").build();
        var request = ExNotificationSendRequestDto.builder()
                .receiver(receiver).type(ExNotificationSendRequestDto.TypeEnum.EMAIL).text(text).build();

        try {
            var result = notificationClient.sending(request);
            log.info("Service: notification send response: {}", result);
        } catch (Exception e) {
            log.error("Service: Cannot send notification: {}", request, e);
        }
    }
}
