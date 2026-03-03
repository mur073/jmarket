package uz.uzumtech.jmarket.orders.component.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import uz.uzumtech.jmarket.orders.generated.dto.OrderCanceledEventDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCanceledEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.kafka.topic.order-canceled}")
    private String topic;

    public void sendEvent(OrderCanceledEventDto event) {
        log.info("Producer: send order cancel event to topic = {} for orderId = {}", topic, event.getId());
        try {
            var record = new ProducerRecord<>(topic, event.getCustomerId().toString(), mapper.writeValueAsString(event));
            kafkaTemplate.send(record);
        } catch (Exception e) {
            log.error("Producer: cannot send order cancel event to topic = {}, error = {}", topic, e.getMessage());
        }
    }
}
