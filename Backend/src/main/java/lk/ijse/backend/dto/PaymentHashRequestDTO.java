package lk.ijse.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentHashRequestDTO {
    @NotBlank(message = "Order ID is required")
    private String order_id;

    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;

    private String customer_id;
    private String timestamp;
}
