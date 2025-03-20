package lk.ijse.backend.controller;

import lk.ijse.backend.entity.ReturnRequest;
import lk.ijse.backend.service.impl.ReturnServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/return-requests")
public class ReturnController {

    @Autowired
    private ReturnServiceImpl returnRequestService;

    @GetMapping("/getAll")
    public List<ReturnRequest> getAllReturnRequests() {
        return returnRequestService.getAllReturnRequests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnRequest> getReturnRequestById(@PathVariable Long id) {
        return returnRequestService.getReturnRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReturnRequest createReturnRequest(@RequestBody ReturnRequest returnRequest) {
        return returnRequestService.createReturnRequest(returnRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnRequest> updateReturnRequest(@PathVariable Long id, @RequestBody ReturnRequest returnRequestDetails) {
        return ResponseEntity.ok(returnRequestService.updateReturnRequest(id, returnRequestDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturnRequest(@PathVariable Long id) {
        returnRequestService.deleteReturnRequest(id);
        return ResponseEntity.noContent().build();
    }
}