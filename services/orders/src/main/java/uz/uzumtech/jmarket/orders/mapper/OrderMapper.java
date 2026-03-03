package uz.uzumtech.jmarket.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import uz.uzumtech.common.client.products.dto.ExProductReserveRequestInnerDto;
import uz.uzumtech.common.client.products.dto.ExProductResponseDto;
import uz.uzumtech.jmarket.orders.entity.Order;
import uz.uzumtech.jmarket.orders.entity.OrderItem;
import uz.uzumtech.jmarket.orders.generated.dto.OrderCanceledEventDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderCreateRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderItemRequestDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderItemResponseDto;
import uz.uzumtech.jmarket.orders.generated.dto.OrderResponseDto;

import java.time.Instant;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    Order toOrderEntity(UUID customerId, OrderCreateRequestDto request);

    @Mapping(target = "totalAmount", expression = "java( order.getTotalAmount() / 100.0 )")
    OrderResponseDto toOrderDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "price", expression = "java( (long)(product.getPrice() * 100) )")
    OrderItem toOrderItemEntity(ExProductResponseDto product, int quantity);

    @Mapping(target = "price", expression = "java( orderItem.getPrice() / 100.0 )")
    OrderItemResponseDto toOrderItemDto(OrderItem orderItem);

    OrderCanceledEventDto toOrderCanceledEvent(Order order);

    ExProductReserveRequestInnerDto toProductReserveRequest(OrderItemRequestDto itemRequest);

    default Long instantToLong(Instant date) {
        if (date == null) {
            return null;
        }
        return date.getEpochSecond();
    }
}
