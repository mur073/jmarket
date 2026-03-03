package uz.uzumtech.jmarket.products.component.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import uz.uzumtech.jmarket.products.generated.dto.OrderCanceledEventDto;
import uz.uzumtech.jmarket.products.service.ProductService;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableKafka
public class OrderCanceledEventListener {

    private final ProductService service;
    private final ObjectMapper mapper;

    @KafkaListener(topics = "${spring.kafka.topic.order-canceled}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenOrderCanceledEvent(String event) {
        try {
            log.info("Listener: received order cancel event: {}", event);
            var eventDto = mapper.readValue(event, OrderCanceledEventDto.class);

            for (var orderItem : eventDto.getItems()) {
                service.increaseQuantity(orderItem.getProductId(), orderItem.getQuantity());
            }
        } catch (Exception e) {
            log.error("Listener: cannot process event = {}: {}", event, e.getMessage());
        }
    }
}
