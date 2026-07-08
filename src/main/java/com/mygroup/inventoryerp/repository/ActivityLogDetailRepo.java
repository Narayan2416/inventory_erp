package com.mygroup.inventoryerp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mygroup.inventoryerp.entity.ActivityLogDetail;

public interface ActivityLogDetailRepo extends JpaRepository<ActivityLogDetail,Integer>{
    
}
