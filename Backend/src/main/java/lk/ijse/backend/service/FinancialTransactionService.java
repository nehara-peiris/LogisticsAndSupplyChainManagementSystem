package lk.ijse.backend.service;

import lk.ijse.backend.entity.FinancialTransaction;

import java.util.List;
import java.util.Optional;

public interface FinancialTransactionService {

    List<FinancialTransaction> getAllTransactions();

    Optional<FinancialTransaction> getTransactionById(Long id);

    FinancialTransaction createTransaction(FinancialTransaction transaction);

    FinancialTransaction updateTransaction(Long id, FinancialTransaction transactionDetails);

    void deleteTransaction(Long id);
}