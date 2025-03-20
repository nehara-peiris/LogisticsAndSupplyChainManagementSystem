package lk.ijse.backend.repo;

import lk.ijse.backend.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepo  extends JpaRepository<PurchaseOrder, Long> {
}
