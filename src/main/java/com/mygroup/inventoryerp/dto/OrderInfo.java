package com.mygroup.inventoryerp.dto;

import java.util.List;

public class OrderInfo {
    private int customerId;
    private boolean status;
    private List<OrderedProduct> products;

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<OrderedProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderedProduct> products) {
        this.products = products;
    }
}
