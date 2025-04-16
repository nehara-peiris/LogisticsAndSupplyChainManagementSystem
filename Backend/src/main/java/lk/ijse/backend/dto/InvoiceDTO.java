package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private String invoiceNumber;
    private Date invoiceDate;
    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String customerName;
    private String customerAddress;
    private String customerContact;
    private List<InvoiceItemDTO> items;
    private Double totalAmount;
}
