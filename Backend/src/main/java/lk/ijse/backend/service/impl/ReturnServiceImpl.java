package lk.ijse.backend.service.impl;

import lk.ijse.backend.entity.ReturnRequest;
import lk.ijse.backend.repo.ReturnRepo;
import lk.ijse.backend.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReturnServiceImpl implements ReturnService {
    @Autowired
    private ReturnRepo returnRequestRepository;

    public List<ReturnRequest> getAllReturnRequests() {
        return returnRequestRepository.findAll();
    }

    public Optional<ReturnRequest> getReturnRequestById(Long id) {
        return returnRequestRepository.findById(id);
    }

    public ReturnRequest createReturnRequest(ReturnRequest returnRequest) {
        return returnRequestRepository.save(returnRequest);
    }

    public ReturnRequest updateReturnRequest(Long id, ReturnRequest returnRequestDetails) {
        ReturnRequest returnRequest = returnRequestRepository.findById(id).orElseThrow();
        returnRequest.setRequestDate(returnRequestDetails.getRequestDate());
        returnRequest.setReason(returnRequestDetails.getReason());
        returnRequest.setStatus(returnRequestDetails.getStatus());
        returnRequest.setOrder(returnRequestDetails.getOrder());
        returnRequest.setShipment(returnRequestDetails.getShipment());
        return returnRequestRepository.save(returnRequest);
    }

    public void deleteReturnRequest(Long id) {
        returnRequestRepository.deleteById(id);
    }
}
