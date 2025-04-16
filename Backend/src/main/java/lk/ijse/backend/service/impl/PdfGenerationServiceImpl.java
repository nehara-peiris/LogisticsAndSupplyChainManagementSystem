package lk.ijse.backend.service.impl;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lk.ijse.backend.dto.InvoiceDTO;
import lk.ijse.backend.dto.InvoiceItemDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfGenerationServiceImpl {

    public byte[] generatePdfFromInvoice(InvoiceDTO invoice) {
        try {
            String html = generateHtmlInvoice(invoice);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, "");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private String generateHtmlInvoice(InvoiceDTO invoice) {
        // Generate HTML string manually using String concatenation or StringBuilder
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial; margin: 20px; }
                    table { width: 100%%; border-collapse: collapse; }
                    th, td { border: 1px solid #ddd; padding: 8px; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <h1>Invoice #%s</h1>
                <p>Date: %s</p>
                
                <h3>Bill To:</h3>
                <p>%s<br>%s</p>
                
                <table>
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        %s
                    </tbody>
                </table>
                
                <h3>Grand Total: $%.2f</h3>
            </body>
            </html>
            """,
                invoice.getInvoiceNumber(),
                invoice.getInvoiceDate().toString(),
                invoice.getCustomerName(),
                invoice.getCustomerAddress(),
                generateInvoiceItemsHtml(invoice.getItems()),
                invoice.getTotalAmount()
        );
    }

    private String generateInvoiceItemsHtml(List<InvoiceItemDTO> items) {
        StringBuilder sb = new StringBuilder();
        for (InvoiceItemDTO item : items) {
            sb.append(String.format("""
                <tr>
                    <td>%s</td>
                    <td>%d</td>
                    <td>$%.2f</td>
                    <td>$%.2f</td>
                </tr>
                """,
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTotal()
            ));
        }
        return sb.toString();
    }
}