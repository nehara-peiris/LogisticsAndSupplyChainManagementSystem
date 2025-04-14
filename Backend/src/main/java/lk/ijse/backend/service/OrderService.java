package lk.ijse.backend.service;

import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderResponseDTO;
import lk.ijse.backend.entity.Orders;

import java.util.List;

public interface OrderService {
    Orders placeOrder(OrderDTO orderDTO);
    List<Orders> getAllOrders();
    Orders getOrderById(Long id);
    OrderResponseDTO updateOrderStatus(Long orderId, String status);

}
