package com.mygroup.inventoryerp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mygroup.inventoryerp.entity.User;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

}