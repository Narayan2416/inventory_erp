package com.mygroup.inventoryerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.ActivityLog;

public interface ActivityLogRepo extends JpaRepository<ActivityLog,Integer>{

    
} 