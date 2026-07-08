package com.mygroup.inventoryerp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.mygroup.inventoryerp.dto.InventoryInfo;
import com.mygroup.inventoryerp.entity.Inventory;
import com.mygroup.inventoryerp.services.InventoryService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/addinventory")
    public Map<String, Object> addInventory(@RequestBody InventoryInfo inventoryInfo, HttpSession session) {
        return inventoryService.addInventory(inventoryInfo, session);
    }

    @GetMapping("/getinventory")
    public List<Inventory> getAllInventory() {
        return inventoryService.getAllInventory();
    }

    @GetMapping("/getinventory/{id}")
    public Inventory getInventoryById(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id);
    }

    @PutMapping("/editinventory/{id}")
    public Map<String, Object> editInventory(@PathVariable Integer id,@RequestBody InventoryInfo inventoryInfo,HttpSession session) {

        Inventory updatedInventory = inventoryService.editInventory(id,inventoryInfo,session);

        if (updatedInventory == null) {
            return Map.of(
                    "message", "Inventory not found or duplicate product-location exists",
                    "valid", false
            );
        }

        return Map.of(
                "message", "Inventory updated successfully",
                "valid", true
        );
    }

    @DeleteMapping("/deleteinventory/{id}")
    public Map<String, Object> deleteInventory(@PathVariable Integer id) {

        Inventory inventory = inventoryService.getInventoryById(id);

        if (inventory == null) {
            return Map.of(
                    "message", "Inventory not found",
                    "valid", false
            );
        }

        inventoryService.deleteInventory(id);

        return Map.of(
                "message", "Inventory deleted successfully",
                "valid", true
        );
    }
}