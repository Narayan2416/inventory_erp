package com.mygroup.inventoryerp.controller;

import org.springframework.web.bind.annotation.*;

import com.mygroup.inventoryerp.dto.OrderedProduct;
import com.mygroup.inventoryerp.entity.OrderItem;
import com.mygroup.inventoryerp.services.OrderService;

import java.util.*;

@RestController
@RequestMapping("/orderitems")
public class OrderItemController {

    private OrderService orderItemService;

    public OrderItemController(OrderService orderItemService) {
        this.orderItemService = orderItemService;
    }


    @GetMapping("/getorderitem/{id}")
    public OrderItem getOrderItem(@PathVariable int id){
        return orderItemService.getOrderItemById(id);
    }


    @PutMapping("/editorderitem/{id}")
    public Map<String,Object> editOrderItem(@PathVariable int id,@RequestBody OrderedProduct dto){
        Map<String,Object> response = new HashMap<>();
        OrderItem item =orderItemService.editOrderItem(id,dto);
        if(item!=null){
            response.put("valid",true);
            response.put("message","Order Item Updated");
        }
        else{
            response.put("valid",false);
            response.put("message","Order Item Not Found");
        }
        return response;
    }


    @DeleteMapping("/deleteorderitem/{id}")
    public Map<String,Object> deleteOrderItem(@PathVariable int id){
        Map<String,Object> response = new HashMap<>();
        boolean deleted =orderItemService.deleteOrderItem(id);
        response.put("valid",deleted);
        response.put("message",deleted ?"Deleted Successfully":"Order Item Not Found");
        return response;
    }
}