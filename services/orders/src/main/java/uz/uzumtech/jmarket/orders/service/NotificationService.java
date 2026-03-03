package uz.uzumtech.jmarket.orders.service;

import uz.uzumtech.jmarket.orders.entity.Order;

public interface NotificationService {

    void orderCreated(String email, Order order);
}
