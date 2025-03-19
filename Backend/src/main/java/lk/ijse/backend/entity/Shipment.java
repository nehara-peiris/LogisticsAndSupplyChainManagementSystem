package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trackingNumber;
    private Date shippedDate;
    private String shipmentStatus;
    private String destinationAddress;
    private LocalDateTime deliveryDate;
    private String carrier;
    private String  status;
    private String notes;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;

}
