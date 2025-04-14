package lk.ijse.backend.controller;

import jakarta.transaction.Transactional;
import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderResponseDTO;
import lk.ijse.backend.dto.OrderStatusUpdateDTO;
import lk.ijse.backend.entity.Orders;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:63342")
public class OrdersController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody OrderDTO orderDTO) {
        Orders order = orderService.placeOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getAll")
    @Transactional
    public ResponseEntity<?> getAllOrders() {
        try {
            List<OrderResponseDTO> orders = orderService.getAllOrders()
                    .stream()
                    .map(order -> new OrderResponseDTO(
                            order.getId(),
                            order.getCustomer() != null ? order.getCustomer().getName() : null,
                            order.getOrderDate(),
                            order.getTotalAmount(),
                            order.getStatus()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch orders: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Orders order = orderService.getOrderById(id);
            if (order != null) {
                return ResponseEntity.ok(order);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch order: " + e.getMessage());
        }
    }

/*    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDTO statusUpdate) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, statusUpdate.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update order status: " + e.getMessage());
        }
    }*/

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderStatusUpdateDTO statusUpdate) {
        try {
            OrderResponseDTO updatedOrder = orderService.updateOrderStatus(orderId, statusUpdate.getStatus());
            return ResponseEntity.ok(updatedOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update order status: " + e.getMessage());
        }
    }
}