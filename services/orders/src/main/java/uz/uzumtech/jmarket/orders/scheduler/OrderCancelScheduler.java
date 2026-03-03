package uz.uzumtech.jmarket.orders.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.jmarket.orders.component.kafka.producer.OrderCanceledEventProducer;
import uz.uzumtech.jmarket.orders.mapper.OrderMapper;
import uz.uzumtech.jmarket.orders.repository.OrderRepository;

import static java.time.Instant.now;
import static uz.uzumtech.jmarket.orders.generated.dto.OrderStatusDto.CANCELLED;
import static uz.uzumtech.jmarket.orders.generated.dto.OrderStatusDto.CREATED;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCancelScheduler {

    private final OrderCanceledEventProducer producer;
    private final OrderRepository repository;
    private final OrderMapper mapper;

    private static final long ONE_HOUR = 3600;

    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(cron = "0 * * * * *")
    @SchedulerLock(
            name = "cancel_unpaid_orders_task",
            lockAtLeastFor = "30s",
            lockAtMostFor = "5m"
    )
    @Transactional
    public void cancelUnpaidOrders() {
        log.info("Scheduler: running cancelUnpaidOrders");

        var orders = repository.findByStatusAndCreatedAtBefore(CREATED, now().minusSeconds(ONE_HOUR));

        for (var order : orders) {
            order.setStatus(CANCELLED);
            repository.save(order);

            producer.sendEvent(mapper.toOrderCanceledEvent(order));
        }

        log.info("Scheduler: finished cancelUnpaidOrders for {} orders", orders.size());
    }
}
