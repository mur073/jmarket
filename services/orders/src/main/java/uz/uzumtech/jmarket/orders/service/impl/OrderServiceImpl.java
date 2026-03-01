package uz.uzumtech.jmarket.orders.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzumtech.common.client.products.ProductApiClient;
import uz.uzumtech.common.client.products.dto.ExProductResponseDto;
import uz.uzumtech.common.error.exception.CommonException;
import uz.uzumtech.jmarket.orders.generated.dto.OrderCreateRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderItemRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderPageResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderStatusUpdateRequestDto;
import uz.uzumtech.jmarket.orders.mapper.OrderMapper;
import uz.uzumtech.jmarket.orders.repository.OrderRepository;
import uz.uzumtech.jmarket.orders.service.OrderService;

import java.util.UUID;

import static uz.uzumtech.jmarket.orders.constant.AppError.ERROR_ORDER_INVALID_STATUS;
import static uz.uzumtech.jmarket.orders.constant.AppError.ERROR_ORDER_NOT_FOUND;
import static uz.uzumtech.jmarket.orders.generated.dto.OrderStatusDto.CANCELLED;
import static uz.uzumtech.jmarket.orders.generated.dto.OrderStatusDto.DELIVERED;
import static uz.uzumtech.jmarket.orders.security.SecurityUtils.getCurrentUserOrThrow;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductApiClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public OrderResponseDto getById(UUID orderId) {
        var currentUser = getCurrentUserOrThrow();

        return repository.findByIdAndCustomerId(orderId, currentUser)
                .map(mapper::toOrderDto)
                .orElseThrow(() -> new CommonException(ERROR_ORDER_NOT_FOUND));
    }

    @Override
    public OrderPageResponseDto getOrdersByCustomer(Integer page, Integer size) {
        var customer = getCurrentUserOrThrow();
        var pageRequest = PageRequest.of(page, size);

        var orders = repository.findAllByCustomerId(customer, pageRequest);

        return OrderPageResponseDto.builder()
                .page(page)
                .pageSize(size)
                .totalItems((int) orders.getTotalElements())
                .totalPages(orders.getTotalPages())
                .items(orders.get().map(mapper::toOrderDto).toList())
                .build();
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto request) {
        var currentUser = getCurrentUserOrThrow();
        var reserveRequest = request.getItems().stream().map(mapper::toProductReserveRequest).toList();
        var products = productClient.productsReservePost(reserveRequest);

        var order = mapper.toOrderEntity(currentUser, request);

        products.forEach(product -> {
            var quantity = request.getItems().stream()
                    .filter(item -> product.getId().equals(item.getProductId()))
                    .findFirst()
                    .map(OrderItemRequestDto::getQuantity)
                    .orElseThrow();

            var orderItem = mapper.toOrderItemEntity(product, quantity);

            order.addItem(orderItem);
            orderItem.setOrder(order);
        });

        var totalAmount = products.stream().map(ExProductResponseDto::getPrice).reduce(0.0, Double::sum);
        order.setTotalAmount((long) (totalAmount * 100));

        return mapper.toOrderDto(repository.save(order));
    }

    @Override
    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatusUpdateRequestDto request) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new CommonException(ERROR_ORDER_NOT_FOUND));

        if (CANCELLED.equals(request.getStatus()) || DELIVERED.equals(request.getStatus())) {
            throw new CommonException(ERROR_ORDER_INVALID_STATUS);
        }

        order.setStatus(request.getStatus());
    }
}
