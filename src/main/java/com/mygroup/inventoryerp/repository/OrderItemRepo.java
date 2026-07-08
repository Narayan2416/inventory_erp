package com.mygroup.inventoryerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {

     
}