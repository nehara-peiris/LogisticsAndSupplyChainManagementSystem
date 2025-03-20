package lk.ijse.backend.repo;

import lk.ijse.backend.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepo  extends JpaRepository<Orders, Long> {
}
