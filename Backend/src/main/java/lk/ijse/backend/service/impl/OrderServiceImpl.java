package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderResponseDTO;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.exception.ResourceNotFoundException;
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
        Customer customer = customerRepository.findByEmail(orderDTO.getCustomerEmail())
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

                    if (product.getQuantity() < productDTO.getQuantity()) {
                        throw new RuntimeException("Insufficient quantity for product: " + product.getName() +
                                ". Available: " + product.getQuantity() +
                                ", Requested: " + productDTO.getQuantity());
                    }

                    product.setQuantity(product.getQuantity() - productDTO.getQuantity());
                    productRepository.save(product);

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
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {
        // Validate status
        if (!List.of("PENDING", "PROCESSING", "COMPLETED", "CANCELLED").contains(status)) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        // Find the order
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Update status
        order.setStatus(status);
        Orders updatedOrder = orderRepository.save(order);

        // Convert to DTO and return
        return new OrderResponseDTO(
                updatedOrder.getId(),
                updatedOrder.getCustomer() != null ? updatedOrder.getCustomer().getName() : null,
                updatedOrder.getOrderDate(),
                updatedOrder.getTotalAmount(),
                updatedOrder.getStatus()
        );
    }


}
