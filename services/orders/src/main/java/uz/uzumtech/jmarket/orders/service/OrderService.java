package uz.uzumtech.jmarket.orders.service;

import uz.uzumtech.jmarket.orders.generated.dto.OrderCreateRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderPageResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderStatusUpdateRequestDto;

import java.util.UUID;

public interface OrderService {

    OrderResponseDto getById(UUID orderId);

    OrderPageResponseDto getOrdersByCustomer(Integer page, Integer size);

    OrderResponseDto createOrder(OrderCreateRequestDto request);

    void updateOrderStatus(UUID orderId, OrderStatusUpdateRequestDto request);
}
