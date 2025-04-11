// OrderController.java
package lk.ijse.backend.controller;

import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.entity.Orders;
import lk.ijse.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody OrderDTO orderDTO) {
        Orders order = orderService.placeOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Orders>> getAllOrders() {
        List<Orders> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Orders order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

}