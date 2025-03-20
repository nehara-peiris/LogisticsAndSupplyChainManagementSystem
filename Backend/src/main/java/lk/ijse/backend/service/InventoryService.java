package lk.ijse.backend.service;

import lk.ijse.backend.entity.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAllInventories();

    Optional<Inventory> getInventoryById(Long id);

    Inventory createInventory(Inventory inventory);
    Inventory updateInventory(Long id, Inventory inventoryDetails);
    void deleteInventory(Long id);
}
