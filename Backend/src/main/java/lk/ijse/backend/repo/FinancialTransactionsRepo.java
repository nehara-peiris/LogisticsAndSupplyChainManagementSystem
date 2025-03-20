package lk.ijse.backend.repo;

import lk.ijse.backend.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialTransactionsRepo  extends JpaRepository<FinancialTransaction, Long> {
}
