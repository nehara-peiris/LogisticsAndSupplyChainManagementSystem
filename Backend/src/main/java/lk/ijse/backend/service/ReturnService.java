package lk.ijse.backend.service;

import lk.ijse.backend.entity.ReturnRequest;

import java.util.List;
import java.util.Optional;

public interface ReturnService {
    List<ReturnRequest> getAllReturnRequests();

    Optional<ReturnRequest> getReturnRequestById(Long id);

    ReturnRequest createReturnRequest(ReturnRequest returnRequest);

    ReturnRequest updateReturnRequest(Long id, ReturnRequest returnRequestDetails);

    void deleteReturnRequest(Long id);
}