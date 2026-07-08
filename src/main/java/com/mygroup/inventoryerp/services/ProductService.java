package com.mygroup.inventoryerp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.entity.Product;
import com.mygroup.inventoryerp.repository.ProductRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class ProductService {
    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo){
        this.productRepo=productRepo;
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }

    public Product getProductById(Integer id){
        return productRepo.findById(id).orElse(null);
    }

    public List<String> getAllCompanies(){
        return productRepo.findAllCompanies();
    }

    public List<String> getAllProductTypes(){
        return productRepo.findAllProductType();
    }

    public Map<String,Object> addProduct(Product product){
        productRepo.save(product);
        Map<String,Object> response=new HashMap<>();
        response.put("message","added successfully");
        response.put("valid",true);
        return response;
    }

    public Product editProduct(Integer id, Product updatedProduct,HttpSession session) {

        Product product = productRepo.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        product.setProductName(updatedProduct.getProductName());
        product.setProductType(updatedProduct.getProductType());
        product.setProductCompany(updatedProduct.getProductCompany());
        product.setProductPrice(updatedProduct.getProductPrice());

        return productRepo.save(product);
    }

    public void deleteProduct(Integer id){
        productRepo.deleteById(id);
    }

}
