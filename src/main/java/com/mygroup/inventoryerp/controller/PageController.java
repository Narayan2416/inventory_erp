package com.mygroup.inventoryerp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;





@Controller
public class PageController {

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String getAdminDashboard() {
        return "dashboard";
    }

    @GetMapping("/dashboard/users")
    public String getUserDashboard() {
        return "user";
    }

    @GetMapping("dashboard/products")
    public String getProductDashboard() {
        return "product";
    }

    @GetMapping("dashboard/inventory")
    public String getInventoryDashboard() {
        return "inventory";
    }

    @GetMapping("dashboard/customers")
    public String getCustomerDashboard() {
        return "customer";
    }

    @GetMapping("/dashboard/orders")
    public String getOrderDashboard() {
        return "order";
    }
    
    
    
    
    


}