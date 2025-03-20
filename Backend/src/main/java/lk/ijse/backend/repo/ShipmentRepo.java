package lk.ijse.backend.repo;

import lk.ijse.backend.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepo  extends JpaRepository<Shipment, Long> {
}
