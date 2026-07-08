package com.mygroup.inventoryerp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.OrderInfo;
import com.mygroup.inventoryerp.dto.OrderedProduct;
import com.mygroup.inventoryerp.entity.Order;
import com.mygroup.inventoryerp.entity.OrderItem;
import com.mygroup.inventoryerp.entity.Product;
import com.mygroup.inventoryerp.repository.OrderItemRepo;
import com.mygroup.inventoryerp.repository.OrderRepo;

@Service
public class OrderService {
    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    private CustomerService customerService;
    private ProductService productService;
    private InventoryService inventoryService;

    public OrderService(OrderRepo orderRepo,OrderItemRepo orderItemRepo,CustomerService customerService, ProductService productService, InventoryService inventoryService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo=orderItemRepo;
        this.customerService=customerService;
        this.productService=productService;
        this.inventoryService=inventoryService;
    }

    public Map<String,Object> addOrder(OrderInfo orderInfo) {
        Order order = new Order();
        order.setCustomer(customerService.getCustomerById(orderInfo.getCustomerId()));

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderedProduct product : orderInfo.getProducts()) {
            OrderItem item = new OrderItem();
            item.setProduct(productService.getProductById(product.getProductId()));
            item.setQuantity(product.getQuantity());
            item.setOrder(order);
            item.setItemTotal((int)(item.getProduct().getProductPrice() * item.getQuantity()));
            orderItems.add(item);
        }
        order.setOrderItems(orderItems);
        order.setOrderDate();
        Map<String,Object> res=new HashMap<>();
        res.put("message","Order added successfully");
        res.put("valid",true);

        orderRepo.save(order);
        return res;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(int id) {
        return orderRepo.findById(id).orElse(null);
    }

    public Order editOrder(int id, OrderInfo orderInfo) {
        Optional<Order> existingOrder = orderRepo.findById(id);
        if (!existingOrder.isPresent()) return null;
        Order order=existingOrder.get();
        order.setOrderId(id);
        order.setCustomer(customerService.getCustomerById(orderInfo.getCustomerId()));
    boolean oldStatus = order.getStatus();
    boolean newStatus = orderInfo.getStatus();

    if (oldStatus != newStatus) {
        if (newStatus) {
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.decreaseStock(
                    item.getProduct().getProductId(),
                    item.getQuantity()
                );
            }
        }
        else {
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.increaseStock(
                    item.getProduct().getProductId(),
                    item.getQuantity()
                );
            }
        }

        order.setStatus(newStatus);
    }

        return orderRepo.save(order);
    }

    public void deleteOrder(int id) {
        Order order = getOrderById(id);
        if (order != null) {
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.increaseStock(
                    item.getProduct().getProductId(),
                    item.getQuantity()
                );
            }
        }
        orderRepo.deleteById(id);
    }

    public OrderItem getOrderItemById(int id) {
        return orderItemRepo.findById(id).orElse(null);
    }


    public OrderItem editOrderItem(int id, OrderedProduct dto) {

        Optional<OrderItem> existing = orderItemRepo.findById(id);
        if(existing.isEmpty())
            return null;

        OrderItem item = existing.get();
        if(item.getOrder().getStatus())inventoryService.increaseStock(item.getProduct().getProductId(), item.getQuantity());
        Product product =productService.getProductById(dto.getProductId());

        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setItemTotal((int)(product.getProductPrice()*dto.getQuantity()));
        if(item.getOrder().getStatus()) inventoryService.decreaseStock(product.getProductId(), dto.getQuantity());
        return orderItemRepo.save(item);

    }

    public boolean deleteOrderItem(int id){
        if(orderItemRepo.existsById(id)){
            OrderItem item = orderItemRepo.findById(id).orElse(null);
            if(item != null && item.getOrder().getStatus()) {
                inventoryService.increaseStock(item.getProduct().getProductId(), item.getQuantity());
            }
            orderItemRepo.deleteById(id);
            return true;
        }
        return false;

    }

}
