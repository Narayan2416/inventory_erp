package com.mygroup.inventoryerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.Order;

public interface OrderRepo extends JpaRepository<Order,Integer>{
    
}
