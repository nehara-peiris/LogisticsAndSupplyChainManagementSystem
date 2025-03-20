package lk.ijse.backend.service.impl;

import lk.ijse.backend.entity.FinancialTransaction;
import lk.ijse.backend.repo.FinancialTransactionsRepo;
import lk.ijse.backend.service.FinancialTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinancialTransactionServiceImpl implements FinancialTransactionService {
    @Autowired
    private FinancialTransactionsRepo financialTransactionRepository;

    public List<FinancialTransaction> getAllTransactions() {
        return financialTransactionRepository.findAll();
    }

    public Optional<FinancialTransaction> getTransactionById(Long id) {
        return financialTransactionRepository.findById(id);
    }

    public FinancialTransaction createTransaction(FinancialTransaction transaction) {
        return financialTransactionRepository.save(transaction);
    }

    public FinancialTransaction updateTransaction(Long id, FinancialTransaction transactionDetails) {
        FinancialTransaction transaction = financialTransactionRepository.findById(id).orElseThrow();
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setTransactionDate(transactionDetails.getTransactionDate());
        transaction.setType(transactionDetails.getType());
        transaction.setCustomer(transactionDetails.getCustomer());
        return financialTransactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        financialTransactionRepository.deleteById(id);
    }
}
