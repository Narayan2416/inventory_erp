package com.mygroup.inventoryerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer> {
    
}
