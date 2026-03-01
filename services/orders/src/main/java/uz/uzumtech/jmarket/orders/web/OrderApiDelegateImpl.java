package uz.uzumtech.jmarket.orders.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.uzumtech.jmarket.orders.generated.api.OrdersApiDelegate;
import uz.uzumtech.jmarket.orders.generated.dto.OrderCreateRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderPageResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderStatusUpdateRequestDto;
import uz.uzumtech.jmarket.orders.service.OrderService;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApiDelegateImpl implements OrdersApiDelegate {

    private final OrderService service;

    @Override
    public ResponseEntity<OrderResponseDto> getOrderById(UUID orderId) {
        log.info("REST: getOrderById for orderId = {}", orderId);
        return ok(service.getById(orderId));
    }

    @Override
    public ResponseEntity<OrderResponseDto> orderCreate(OrderCreateRequestDto request) {
        log.info("REST: orderCreate request");
        return ok(service.createOrder(request));
    }

    @Override
    public ResponseEntity<OrderPageResponseDto> ordersGet(Integer page, Integer size) {
        log.info("REST: ordersGet page = {}, size = {}", page, size);
        return ok(service.getOrdersByCustomer(page, size));
    }

    @Override
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> updateOrderStatus(UUID orderId, OrderStatusUpdateRequestDto request) {
        log.info("REST: updateOrderStatus orderId = {}, status = {}", orderId, request.getStatus());
        service.updateOrderStatus(orderId, request);
        return OrdersApiDelegate.super.updateOrderStatus(orderId, request);
    }
}
