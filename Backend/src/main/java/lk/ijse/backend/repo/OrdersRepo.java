package lk.ijse.backend.repo;

import lk.ijse.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdersRepo extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o JOIN FETCH o.customer JOIN FETCH o.orderItems WHERE o.id = :orderId")
    Optional<Orders> findOrderWithDetails(@Param("orderId") Long orderId);
}