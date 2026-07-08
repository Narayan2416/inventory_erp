package com.mygroup.inventoryerp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {

    Optional<Customer> findByCustomerPhone(String customerPhone);

    
} 
