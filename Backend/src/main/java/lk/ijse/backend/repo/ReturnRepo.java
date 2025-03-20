package lk.ijse.backend.repo;

import lk.ijse.backend.entity.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRepo extends JpaRepository<ReturnRequest,Long> {
}
