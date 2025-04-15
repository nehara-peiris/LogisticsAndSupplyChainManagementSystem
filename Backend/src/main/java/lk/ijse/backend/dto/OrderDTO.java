package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String customerEmail;
    private double totalAmount;
    private List<OrderItemDTO> orderItems;
}
