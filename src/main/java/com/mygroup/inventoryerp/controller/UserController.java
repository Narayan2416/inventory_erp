package com.mygroup.inventoryerp.controller;

import java.util.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mygroup.inventoryerp.dto.UserInfo;
import com.mygroup.inventoryerp.entity.User;
import com.mygroup.inventoryerp.services.UserService;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/adduser")
    public Map<String,Object> addUser(@RequestBody UserInfo userInfo,HttpSession session) {
        return userService.addUser(userInfo,session);
    }

    @GetMapping("/getusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getusers/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping("/edituser/{id}")
    public Map<String,Object> editUser(@PathVariable int id, @RequestBody UserInfo user,HttpSession session) {
        userService.editUser(id, user,session);
        return Map.of("message", "User updated successfully","valid",true);

    }

    @DeleteMapping("/deleteuser/{id}")
    public Map<String,Object> deleteUser(@PathVariable int id,HttpSession session) {
        userService.deleteUser(id,session);
        return Map.of("message", "User deleted successfully", "valid", true);
    }
}
