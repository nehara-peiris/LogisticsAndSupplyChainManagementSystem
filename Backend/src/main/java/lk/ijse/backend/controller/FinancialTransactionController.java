package lk.ijse.backend.controller;

import lk.ijse.backend.entity.FinancialTransaction;
import lk.ijse.backend.service.impl.FinancialTransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class FinancialTransactionController {

    @Autowired
    private FinancialTransactionServiceImpl financialTransactionService;

    @GetMapping("/getAll")
    public List<FinancialTransaction> getAllTransactions() {
        return financialTransactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialTransaction> getTransactionById(@PathVariable Long id) {
        return financialTransactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public FinancialTransaction createTransaction(@RequestBody FinancialTransaction transaction) {
        return financialTransactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialTransaction> updateTransaction(@PathVariable Long id, @RequestBody FinancialTransaction transactionDetails) {
        return ResponseEntity.ok(financialTransactionService.updateTransaction(id, transactionDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        financialTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}