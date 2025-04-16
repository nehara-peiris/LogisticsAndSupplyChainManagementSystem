package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    private String itemNumber;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double total;
}
