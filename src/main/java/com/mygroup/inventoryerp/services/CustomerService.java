package com.mygroup.inventoryerp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.entity.Customer;
import com.mygroup.inventoryerp.repository.CustomerRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepo.findById(id).orElse(null);
    }

    public Map<String, Object> addCustomer(Customer customerInfo, HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Optional<Customer> existingCustomer = customerRepo.findByCustomerPhone(customerInfo.getCustomerPhone());

        if (existingCustomer.isPresent()) {
            response.put("message", "Customer already exists");
            response.put("valid", false);
            return response;
        }

        Customer customer = new Customer();

        customer.setCustomerName(customerInfo.getCustomerName());
        customer.setCustomerEmail(customerInfo.getCustomerEmail());
        customer.setCustomerPhone(customerInfo.getCustomerPhone());
        customer.setAddressLine(customerInfo.getAddressLine());
        customer.setCity(customerInfo.getCity());
        customer.setState(customerInfo.getState());
        customer.setPinCode(customerInfo.getPinCode());

        customerRepo.save(customer);

        response.put("message", "Customer added successfully");
        response.put("valid", true);

        return response;
    }

    public Customer editCustomer(Integer id,Customer customerInfo,HttpSession session) {

        Customer customer = customerRepo.findById(id).orElse(null);

        if (customer == null) {
            return null;
        }

        Optional<Customer> existingCustomer =customerRepo.findByCustomerPhone(customerInfo.getCustomerPhone());

        if (existingCustomer.isPresent()
                && existingCustomer.get().getCustomerId() != id) {
            return null;
        }

        customer.setCustomerName(customerInfo.getCustomerName());
        customer.setCustomerEmail(customerInfo.getCustomerEmail());
        customer.setCustomerPhone(customerInfo.getCustomerPhone());
        customer.setAddressLine(customerInfo.getAddressLine());
        customer.setCity(customerInfo.getCity());
        customer.setState(customerInfo.getState());
        customer.setPinCode(customerInfo.getPinCode());

        return customerRepo.save(customer);
    }

    public void deleteCustomer(Integer id) {
        customerRepo.deleteById(id);
    }
}