package com.mygroup.inventoryerp.controller;

import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;     
import org.springframework.web.bind.annotation.RestController;

import com.mygroup.inventoryerp.dto.LoginRequest;
import com.mygroup.inventoryerp.dto.UserInfo;
import com.mygroup.inventoryerp.entity.Role;
import com.mygroup.inventoryerp.services.RoleService;
import com.mygroup.inventoryerp.services.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;

    public AuthController(UserService userService,RoleService roleService) {
        this.userService = userService;
        this.roleService= roleService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        //System.out.println("login");
        UserInfo defaultUser = new UserInfo();
        defaultUser.setUserName("admin1");
        defaultUser.setPassword("password");
        defaultUser.setUserEmail("default@example.com");
        defaultUser.setRoleId(1);
        userService.addUser(defaultUser,session);


        Map<String,Object> result = userService.login(loginRequest);
        if((boolean) result.get("valid")){
            session.setAttribute("roleId", result.get("roleId"));
            session.setAttribute("userName", result.get("userName"));
            session.setAttribute("userId", result.get("userId"));
            return result;
        }
        else return result;
    }
    
    @GetMapping("/dashboard/menu")
    public Map<String, String> getDashboardMenu(HttpSession session) {
        int roleId=(Integer) session.getAttribute("roleId");
        Role role=roleService.getRoleById(roleId);
        Map<String,String> result=new HashMap<>();
        result.put("users",role.getUsersAccess());
        result.put("customers",role.getCustomersAccess());
        result.put("orders",role.getOrdersAccess());
        //result.put("orderItems",role.getOrdersAccess());
        result.put("inventory",role.getInventoryAccess());
        result.put("products",role.getProductsAccess());
        return result;
    }
    

}
