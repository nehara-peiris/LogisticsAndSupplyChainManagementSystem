package lk.ijse.backend.service;

import lk.ijse.backend.entity.PurchaseOrder;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderService {
    List<PurchaseOrder> getAllPurchaseOrders();

    Optional<PurchaseOrder> getPurchaseOrderById(Long id);

    PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder);

    PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder purchaseOrderDetails);

    void deletePurchaseOrder(Long id);
}
