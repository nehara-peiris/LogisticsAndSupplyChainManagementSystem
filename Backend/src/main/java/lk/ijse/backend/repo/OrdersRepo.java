package lk.ijse.backend.repo;

import lk.ijse.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrdersRepo extends JpaRepository<Orders, Long> {

    // Method to find orders by customer ID
    @Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId")
    List<Orders> findByCustomerId(@Param("customerId") Long customerId);

    // Method to find orders by status
    List<Orders> findByStatus(String status);

    // Optional: Method to find orders with their shipments
    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.shipment WHERE o.id = :orderId")
    Orders findOrderWithShipment(@Param("orderId") Long orderId);
}