package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.repo.CustomerRepository;
import lk.ijse.backend.repo.OrderItemRepo;
import lk.ijse.backend.repo.OrdersRepo;
import lk.ijse.backend.repo.ProductRepository;
import lk.ijse.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersRepo orderRepository;

    @Autowired
    private OrderItemRepo orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Orders placeOrder(OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Orders order = new Orders();
        order.setOrderDate(new Date());
        order.setCustomer(customer);
        order.setTotalAmount(orderDTO.getTotalAmount());

        Orders savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(productDTO -> {
                    Product product = productRepository.findById(productDTO.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(productDTO.getQuantity());
                    orderItem.setPrice(productDTO.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);

        savedOrder.setOrderItems(orderItems);

        return savedOrder;
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Orders updateOrderStatus(Long orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<Orders> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Orders> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

}