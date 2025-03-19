package lk.ijse.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date requestDate;
    private String reason;
    private String status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @OneToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;
}
