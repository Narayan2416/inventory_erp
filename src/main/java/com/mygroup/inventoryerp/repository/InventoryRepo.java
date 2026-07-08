package com.mygroup.inventoryerp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.Inventory;
import com.mygroup.inventoryerp.entity.Product;

public interface InventoryRepo extends JpaRepository<Inventory,Integer>{

    Optional<Inventory> findByProductAndLocation(Product product,String location);

    Optional<Inventory> findByProductProductId(int productId);
}