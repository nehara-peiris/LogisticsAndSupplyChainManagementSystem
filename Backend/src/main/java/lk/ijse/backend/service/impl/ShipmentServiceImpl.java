package lk.ijse.backend.service.impl;

import lk.ijse.backend.entity.Shipment;
import lk.ijse.backend.repo.ShipmentRepo;
import lk.ijse.backend.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    @Autowired
    private ShipmentRepo shipmentRepository;

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Optional<Shipment> getShipmentById(Long id) {
        return shipmentRepository.findById(id);
    }

    public Shipment createShipment(Shipment shipment) {
        return shipmentRepository.save(shipment);
    }

    public Shipment updateShipment(Long id, Shipment shipmentDetails) {
        Shipment shipment = shipmentRepository.findById(id).orElseThrow();
        shipment.setTrackingNumber(shipmentDetails.getTrackingNumber());
        shipment.setShippedDate(shipmentDetails.getShippedDate());
        shipment.setShipmentStatus(shipmentDetails.getShipmentStatus());
        shipment.setDestinationAddress(shipmentDetails.getDestinationAddress());
        shipment.setDeliveryDate(shipmentDetails.getDeliveryDate());
        shipment.setCarrier(shipmentDetails.getCarrier());
        shipment.setStatus(shipmentDetails.getStatus());
        shipment.setNotes(shipmentDetails.getNotes());
        shipment.setOrder(shipmentDetails.getOrder());
        return shipmentRepository.save(shipment);
    }

    public void deleteShipment(Long id) {
        shipmentRepository.deleteById(id);
    }
}