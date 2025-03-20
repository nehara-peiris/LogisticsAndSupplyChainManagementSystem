package lk.ijse.backend.service.impl;

import lk.ijse.backend.entity.Inventory;
import lk.ijse.backend.repo.InventoryRepo;
import lk.ijse.backend.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepo inventoryRepository;

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Long id, Inventory inventoryDetails) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow();
        inventory.setStockLevel(inventoryDetails.getStockLevel());
        inventory.setLastUpdated(inventoryDetails.getLastUpdated());
        inventory.setProduct(inventoryDetails.getProduct());
        return inventoryRepository.save(inventory);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
