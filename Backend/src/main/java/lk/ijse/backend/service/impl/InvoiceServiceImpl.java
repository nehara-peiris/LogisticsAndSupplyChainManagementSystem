package lk.ijse.backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.backend.dto.InvoiceDTO;
import lk.ijse.backend.dto.InvoiceItemDTO;
import lk.ijse.backend.entity.Customer;
import lk.ijse.backend.entity.OrderItem;
import lk.ijse.backend.entity.Orders;
import lk.ijse.backend.repo.OrdersRepo;
import lk.ijse.backend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private final OrdersRepo orderRepository;

    @Autowired
    private PdfGenerationServiceImpl pdfGenerationService;

    public InvoiceServiceImpl(OrdersRepo orderRepository) {
        this.orderRepository = orderRepository;
    }


    public InvoiceDTO generateInvoice(Long orderId) {
        Orders order = orderRepository.findOrderWithDetails(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        return mapOrderToInvoiceDTO(order);
    }

    public String generateHtmlInvoice(Long orderId) {
        InvoiceDTO invoiceDTO = generateInvoice(orderId);
        return generateHtmlFromInvoiceDTO(invoiceDTO);
    }

    private InvoiceDTO mapOrderToInvoiceDTO(Orders order) {
        InvoiceDTO invoice = new InvoiceDTO();

        // Invoice info
        invoice.setInvoiceNumber(order.getId().toString());
        invoice.setInvoiceDate(order.getOrderDate());
        invoice.setTotalAmount(order.getTotalAmount());

        // Company info (static - can be moved to properties)
        invoice.setCompanyName("Your Company Name");
        invoice.setCompanyAddress("123 Business St, Commerce City, 10001");
        invoice.setCompanyPhone("+1 (555) 123-4567");

        // Customer info
        Customer customer = order.getCustomer();
        invoice.setCustomerName(customer.getName());
        invoice.setCustomerAddress(String.format("%s, %s, %s, %s, %s",
                customer.getAddress(),
                customer.getCity(),
                customer.getState(),
                customer.getZipCode(),
                customer.getCountry()));
        invoice.setCustomerContact(customer.getContact());

        // Order items
        List<InvoiceItemDTO> items = order.getOrderItems().stream()
                .map(this::mapOrderItemToInvoiceItemDTO)
                .collect(Collectors.toList());
        invoice.setItems(items);

        return invoice;
    }

    private InvoiceItemDTO mapOrderItemToInvoiceItemDTO(OrderItem orderItem) {
        InvoiceItemDTO item = new InvoiceItemDTO();
        item.setItemNumber(orderItem.getId().toString());
        item.setProductName(orderItem.getProduct().getName());
        item.setQuantity(orderItem.getQuantity());
        item.setPrice(orderItem.getPrice());
        item.setTotal(orderItem.getPrice() * orderItem.getQuantity());
        return item;
    }

    private String generateHtmlFromInvoiceDTO(InvoiceDTO invoice) {
        // Using your template structure
        return String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Invoice</title>
                <style>
                    * {
                        box-sizing: border-box;
                        margin: 0;
                        padding: 0;
                    }

                    body {
                        font-family: 'Poppins', sans-serif;
                        background-color: #1a1a1a;
                        color: #fff;
                        padding: 20px;
                    }

                    .container {
                        max-width: 900px;
                        margin: auto;
                        background: #2a2a2a;
                        padding: 25px;
                        border-radius: 12px;
                        box-shadow: 0 5px 15px rgba(0, 0, 0, 0.5);
                    }

                    h2 {
                        text-align: center;
                        color: #fff;
                        font-weight: 600;
                        margin-bottom: 20px;
                    }

                    .invoice-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        border-bottom: 2px solid #444;
                        padding-bottom: 10px;
                        margin-bottom: 20px;
                    }

                    .invoice-info {
                        font-size: 16px;
                        color: #ccc;
                    }

                    .info-container {
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 20px;
                    }

                    .info-box {
                        width: 48%%;
                        background-color: #333;
                        padding: 15px;
                        border-radius: 8px;
                    }

                    .info-box h3 {
                        margin-bottom: 10px;
                        color: #fff;
                    }

                    .table {
                        width: 100%%;
                        border-collapse: collapse;
                        margin-top: 20px;
                        background-color: #333;
                        color: #fff;
                    }

                    th, td {
                        padding: 14px;
                        text-align: center;
                        border-bottom: 1px solid #444;
                    }

                    th {
                        background-color: #007bff;
                        color: white;
                        font-weight: 600;
                    }

                    td {
                        background-color: #2a2a2a;
                    }

                    .total {
                        text-align: right;
                        font-size: 18px;
                        margin-top: 20px;
                    }
                </style>
            </head>
            <body>
            <div class="container">
                <div class="invoice-header">
                    <h2>Invoice</h2>
                    <div class="invoice-info">
                        <p>Invoice #: %s</p>
                        <p>Date: %s</p>
                    </div>
                </div>

                <div class="info-container">
                    <div class="info-box">
                        <h3>Company Info</h3>
                        <p>%s</p>
                        <p>Phone: %s</p>
                    </div>
                    <div class="info-box">
                        <h3>Customer Info</h3>
                        <p>%s</p>
                        <p>%s</p>
                        <p>Contact: %s</p>
                    </div>
                </div>

                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                        <th>Total</th>
                    </tr>
                    </thead>
                    <tbody>
                    %s
                    </tbody>
                </table>

                <div class="total">
                    <h4>Total amount: <span>$%.2f</span></h4>
                </div>
            </div>
            </body>
            </html>
            """,
                invoice.getInvoiceNumber(),
                new SimpleDateFormat("MM/dd/yyyy").format(invoice.getInvoiceDate()),
                invoice.getCompanyAddress(),
                invoice.getCompanyPhone(),
                invoice.getCustomerName(),
                invoice.getCustomerAddress(),
                invoice.getCustomerContact(),
                generateInvoiceItemsHtml(invoice.getItems()),
                invoice.getTotalAmount()
        );
    }

    private String generateInvoiceItemsHtml(List<InvoiceItemDTO> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            InvoiceItemDTO item = items.get(i);
            sb.append(String.format("""
                <tr>
                    <td>%d</td>
                    <td>%s</td>
                    <td>%d</td>
                    <td>$%.2f</td>
                    <td>$%.2f</td>
                </tr>
                """,
                    i + 1,
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getTotal()
            ));
        }
        return sb.toString();
    }

    public byte[] generatePdfInvoice(Long orderId) {
        InvoiceDTO invoiceDTO = generateInvoice(orderId);
        return pdfGenerationService.generatePdfFromInvoice(invoiceDTO);
    }
}
