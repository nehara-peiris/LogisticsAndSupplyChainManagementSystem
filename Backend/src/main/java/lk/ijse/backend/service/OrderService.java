package lk.ijse.backend.service;

import lk.ijse.backend.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Orders> getAllOrders();

    Optional<Orders> getOrderById(Long id);

    Orders createOrder(Orders order);

    Orders updateOrder(Long id, Orders orderDetails);

    void deleteOrder(Long id);
}
