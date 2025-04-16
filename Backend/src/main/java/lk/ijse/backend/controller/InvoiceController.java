package lk.ijse.backend.controller;

import jakarta.persistence.EntityNotFoundException;
import lk.ijse.backend.dto.InvoiceDTO;
import lk.ijse.backend.service.impl.InvoiceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    private final InvoiceServiceImpl invoiceService;
    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    public InvoiceController(InvoiceServiceImpl invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getInvoice(@PathVariable Long orderId,
                                        @RequestHeader("Authorization") String authHeader) {
        if (!validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            InvoiceDTO invoice = invoiceService.generateInvoice(orderId);
            return ResponseEntity.ok(invoice);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{orderId}/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getInvoiceHtml(@PathVariable Long orderId,
                                            @RequestHeader("Authorization") String authHeader) {
        if (!validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String html = invoiceService.generateHtmlInvoice(orderId);
            return ResponseEntity.ok(html);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice not found for order: " + orderId);
        } catch (Exception e) {
            logger.error("Error generating HTML invoice", e);
            return ResponseEntity.internalServerError()
                    .body("Error generating invoice");
        }
    }

    @GetMapping(value = "/{orderId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long orderId,
                                                @RequestHeader("Authorization") String authHeader) {
        if (!validateToken(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            byte[] pdfBytes = invoiceService.generatePdfInvoice(orderId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=invoice_" + orderId + ".pdf")
                    .body(pdfBytes);
        } catch (Exception e) {
            logger.error("Error generating PDF invoice", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean validateToken(String authHeader) {
        // Implement your JWT validation logic here
        return authHeader != null && authHeader.startsWith("Bearer ");
    }
}