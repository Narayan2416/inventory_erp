package com.mygroup.inventoryerp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mygroup.inventoryerp.entity.Product;
import com.mygroup.inventoryerp.services.ProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/addproduct")
    public Map<String,Object> addProduct(@RequestBody Product product,HttpSession session) {
        int loginedUserId = (Integer) session.getAttribute("userId");
        return productService.addProduct(product, loginedUserId);
    }

    @GetMapping("/getproducts")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/getproducts/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @GetMapping("/companies")
    public ResponseEntity<List<String>> getCompanies() {
        return ResponseEntity.ok(productService.getAllCompanies());
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getProductTypes(){
        return ResponseEntity.ok(productService.getAllProductTypes());
    }
    
    
    @PutMapping("/editproduct/{id}")
    public Map<String, Object> editProduct(@PathVariable Integer id,@RequestBody Product product,HttpSession session) {
        int loginedUserId = (Integer) session.getAttribute("userId");
        Product updatedProduct = productService.editProduct(id, product, loginedUserId);

        if (updatedProduct == null) {
            return Map.of(
                    "message", "Product not found",
                    "valid", false
            );
        }

        return Map.of(
                "message", "Product updated successfully",
                "valid", true
        );
    }

    @DeleteMapping("/deleteproduct/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Integer id,HttpSession session) {
        int loginedUserId = (Integer) session.getAttribute("userId");

        Product product = productService.getProductById(id);

        if (product == null) {
            return Map.of(
                    "message", "Product not found",
                    "valid", false
            );
        }

        productService.deleteProduct(id,loginedUserId);

        return Map.of(
                "message", "Product deleted successfully",
                "valid", true
        );
    }
}