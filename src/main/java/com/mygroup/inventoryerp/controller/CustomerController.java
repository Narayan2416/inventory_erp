package com.mygroup.inventoryerp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.mygroup.inventoryerp.entity.Customer;
import com.mygroup.inventoryerp.services.CustomerService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/addcustomer")
    public Map<String, Object> addCustomer(@RequestBody Customer customerInfo,HttpSession session) {
        return customerService.addCustomer(customerInfo, session);
    }

    @GetMapping("/getcustomers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/getcustomers/{id}")
    public Customer getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id);
    }

    @PutMapping("/editcustomer/{id}")
    public Map<String, Object> editCustomer(@PathVariable Integer id,@RequestBody Customer customerInfo,HttpSession session) {

        Customer customer =customerService.editCustomer(id, customerInfo, session);

        if (customer == null) {
            return Map.of(
                    "message", "Customer not found or phone number already exists",
                    "valid", false
            );
        }

        return Map.of(
                "message", "Customer updated successfully",
                "valid", true
        );
    }

    @DeleteMapping("/deletecustomer/{id}")
    public Map<String, Object> deleteCustomer(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return Map.of(
                    "message", "Customer not found",
                    "valid", false
            );
        }

        customerService.deleteCustomer(id);
        return Map.of(
                "message", "Customer deleted successfully",
                "valid", true
        );
    }
}