package lk.ijse.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lk.ijse.backend.dto.PaymentHashRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final String merchantId = "1230127 "; // PayHere sandbox ID
    private final String merchantSecret = "MzQ4NDAyNDI2ODIxMjYyMjIwNDMzMTUwNTExMTQ4MTgxNjI4MjYxOA=="; // Sandbox secret

    @PostMapping("/hash")
    public ResponseEntity<Map<String, String>> generateHash(@RequestBody Map<String, String> payload) {
        try {
            String orderId = payload.get("order_id");
            String amountStr = payload.get("amount");
            String currency = payload.get("currency");

            // Validate required fields
            if (orderId == null || amountStr == null || currency == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
            }

            // Format amount to 2 decimal places
            DecimalFormat df = new DecimalFormat("0.00");
            String amountFormatted;
            try {
                double amount = Double.parseDouble(amountStr);
                amountFormatted = df.format(amount);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid amount format"));
            }

            // Calculate MD5 hashes
            String innerHash = getMd5(merchantSecret);
            String hash = getMd5(merchantId + orderId + amountFormatted + currency + innerHash);

            System.out.println("Generated Hash: " + hash); // For debugging
            System.out.println("Parameters used: " + merchantId + "|" + orderId + "|" + amountFormatted + "|" + currency + "|" + innerHash);

            return ResponseEntity.ok(Map.of(
                    "hash", hash,
                    "merchant_id", merchantId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handleNotify(HttpServletRequest request) {
        try {
            // Verify sandbox notification
            String sandbox = request.getParameter("sandbox");
            if (!"1".equals(sandbox)) {
                return ResponseEntity.badRequest().body("Invalid sandbox notification");
            }

            // Verify hash
            String md5sig = request.getParameter("md5sig");
            String localMd5 = getMd5(
                    request.getParameter("merchant_id") +
                            request.getParameter("order_id") +
                            request.getParameter("payhere_amount") +
                            request.getParameter("payhere_currency") +
                            request.getParameter("status_code") +
                            getMd5(merchantSecret)
            );

            if (!localMd5.equalsIgnoreCase(md5sig)) {
                return ResponseEntity.badRequest().body("Invalid hash");
            }

            // Process based on status
            String status = request.getParameter("status_code");
            String orderId = request.getParameter("order_id");

            if ("2".equals(status)) {
                // Payment successful
                System.out.println("SANDBOX PAYMENT SUCCESS: " + orderId);
                return ResponseEntity.ok("Payment processed");
            } else {
                // Payment failed
                System.out.println("SANDBOX PAYMENT FAILED: " + orderId);
                return ResponseEntity.ok("Payment failed");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Notification processing failed");
        }
    }

    // Helper method for MD5 hashing
    private static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}