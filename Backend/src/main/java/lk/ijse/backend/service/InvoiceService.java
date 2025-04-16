package lk.ijse.backend.service;

import lk.ijse.backend.dto.InvoiceDTO;
import lk.ijse.backend.entity.Orders;

public interface InvoiceService {
    InvoiceDTO generateInvoice(Long orderId);

    String generateHtmlInvoice(Long orderId);
}
