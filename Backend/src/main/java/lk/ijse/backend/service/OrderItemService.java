package lk.ijse.backend.service;

import lk.ijse.backend.entity.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemService {
    List<OrderItem> getAllOrderItems();

    Optional<OrderItem> getOrderItemById(Long id);

    OrderItem createOrderItem(OrderItem orderItem);

    OrderItem updateOrderItem(Long id, OrderItem orderItemDetails);

    void deleteOrderItem(Long id);
}
