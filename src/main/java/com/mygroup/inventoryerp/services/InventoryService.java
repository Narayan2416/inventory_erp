package com.mygroup.inventoryerp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.InventoryInfo;
import com.mygroup.inventoryerp.entity.Inventory;
import com.mygroup.inventoryerp.entity.Product;
import com.mygroup.inventoryerp.repository.InventoryRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    private final ProductService productService;

    public InventoryService(InventoryRepo inventoryRepo,ProductService productService) {
        this.inventoryRepo = inventoryRepo;
        this.productService = productService;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepo.findAll();
    }

    public Inventory getInventoryById(Integer id) {
        return inventoryRepo.findById(id).orElse(null);
    }

    public Map<String, Object> addInventory(InventoryInfo inventoryInfo,HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Product product = productService.getProductById(inventoryInfo.getProductId());
        if (product == null) {
            response.put("message", "Product not found");
            response.put("valid", false);
            return response;
        }
        Optional<Inventory> existing =inventoryRepo.findByProductAndLocation(product, inventoryInfo.getLocation());

        if (existing.isPresent()) {
            response.put("message", "This product already exists in the selected location");
            response.put("valid", false);
            return response;
        }
        Inventory inventory=new Inventory();

        inventory.setLocation(inventoryInfo.getLocation());
        inventory.setQuantity(inventoryInfo.getQuantity());
        inventory.setProduct(product);
        inventory.setLastUpdated();

        inventoryRepo.save(inventory);

        response.put("message", "Inventory added successfully");
        response.put("valid", true);

        return response;
    }

    public Inventory editInventory(Integer inventoryId,InventoryInfo updatedInventory,HttpSession session) {

        Inventory inventory = inventoryRepo.findById(inventoryId).orElse(null);
        if (inventory == null) {
            return null;
        }

        Product product = productService.getProductById(updatedInventory.getProductId());
        if (product == null) {
            return null;
        }

        Inventory existing=inventoryRepo.findByProductAndLocation(product,updatedInventory.getLocation()).orElse(null);
        if(existing!=null && inventoryId!=existing.getInventoryId()){
            existing.setQuantity(existing.getQuantity()+updatedInventory.getQuantity());
            existing.setLastUpdated();
            inventoryRepo.save(existing);
            deleteInventory(inventoryId);
            
        }
        inventory.setProduct(product);
        inventory.setQuantity(updatedInventory.getQuantity());
        inventory.setLocation(updatedInventory.getLocation());
        inventory.setLastUpdated();

        return inventoryRepo.save(inventory);
    }

    // Delete Inventory
    public void deleteInventory(Integer id) {
        inventoryRepo.deleteById(id);
    }

    public void increaseStock(int productId, int quantity) {
        Optional<Inventory> inventory = inventoryRepo.findByProductProductId(productId);
        if (inventory.isPresent()) {
            Inventory inv = inventory.get();
            inv.setQuantity(inv.getQuantity() + quantity);
            inv.setLastUpdated();
            inventoryRepo.save(inv);
        }
    }

    public Map<String,Object> decreaseStock(int productId, int quantity) {
        Optional<Inventory> inventory = inventoryRepo.findByProductProductId(productId);
        if (inventory.isPresent()) {
            Inventory inv = inventory.get();
            if(inv.getQuantity() < quantity) {
                return Map.of("message", "Not enough stock for product ID: " + productId, "valid", false);
            }
            inv.setQuantity(inv.getQuantity() - quantity);
            inv.setLastUpdated();
            inventoryRepo.save(inv);
        }
        return Map.of("message", "Stock decreased successfully for product ID: " + productId, "valid", true);
    }
}