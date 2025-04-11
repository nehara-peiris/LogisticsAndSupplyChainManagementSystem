package lk.ijse.backend.service;

import lk.ijse.backend.entity.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentService {
    List<Shipment> getAllShipments();

    Optional<Shipment> getShipmentById(Long id);

    Shipment createShipment(Shipment shipment);

    Shipment updateShipment(Long id, Shipment shipmentDetails);

    void deleteShipment(Long id);
}