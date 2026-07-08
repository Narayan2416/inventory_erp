package com.mygroup.inventoryerp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mygroup.inventoryerp.dto.OrderInfo;
import com.mygroup.inventoryerp.entity.Order;
import com.mygroup.inventoryerp.services.OrderService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getorders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/getorders/{id}")
    public Order getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/addorder")
    public Map<String,Object> addOrder(@RequestBody OrderInfo order) {
        return orderService.addOrder(order);
    }
    

    @PutMapping("/editorder/{id}")
    public Map<String,Object> editOrder(@PathVariable int id, @RequestBody OrderInfo order) {
        Order ord=orderService.editOrder(id, order);
        if(ord != null) {
            return Map.of("message", "Order updated successfully", "valid", true);
        }
        return Map.of("message", "Order not found", "valid", false);
    }

    @DeleteMapping("/deleteorder/{id}")
    public Map<String,Object> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return Map.of("valid", true);
    }

}
