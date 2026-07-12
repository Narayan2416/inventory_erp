package com.mygroup.inventoryerp.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityDetailRequest;
import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.entity.Product;
import com.mygroup.inventoryerp.entity.User;
import com.mygroup.inventoryerp.repository.ProductRepo;


@Service
public class ProductService {
    private final UserService userService;
    private ProductRepo productRepo;
    private ActivityLogService activityLogService;

    public ProductService(ProductRepo productRepo, ActivityLogService activityLogService, UserService userService){
        this.productRepo=productRepo;
        this.activityLogService = activityLogService;
        this.userService = userService;
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

    public Map<String,Object> addProduct(Product product,int loginedUserId){
        Product addedProduct = productRepo.save(product);
        Map<String,Object> response=new HashMap<>();
        response.put("message","added successfully");
        response.put("valid",true);

        ActivityLogRequest log = new ActivityLogRequest();
        log.setAction("CREATE");
        log.setTableName("PRODUCTS");
        log.setRecordId(addedProduct.getProductId());
        log.setUserId(loginedUserId);

        User loginedUser = userService.getUserById(loginedUserId);
        activityLogService.addActivityLog(log, loginedUser);

        return response;
    }

    public Product editProduct(Integer id, Product updatedProduct,int loginedUserId) {

        Product product = productRepo.findById(id).orElse(null);

        if (product == null) {
            return null;
        }
        ActivityLogRequest log = new ActivityLogRequest();
        List<ActivityDetailRequest> details = new ArrayList<>();
        boolean flag=false;

        if(!product.getProductName().equals(updatedProduct.getProductName())) {
            flag=true;
            ActivityDetailRequest detail = new ActivityDetailRequest();
            detail.setFieldName("productName");
            detail.setOldValue(product.getProductName());
            detail.setNewValue(updatedProduct.getProductName());
            product.setProductName(updatedProduct.getProductName());
            details.add(detail);
        }
        if(!product.getProductType().equals(updatedProduct.getProductType())) {
            flag=true;
            ActivityDetailRequest detail = new ActivityDetailRequest();
            detail.setFieldName("productType");
            detail.setOldValue(product.getProductType());
            detail.setNewValue(updatedProduct.getProductType());
            product.setProductType(updatedProduct.getProductType());
            details.add(detail);
        }
        if(!product.getProductCompany().equals(updatedProduct.getProductCompany())) {
            flag=true;
            ActivityDetailRequest detail = new ActivityDetailRequest();
            detail.setFieldName("productCompany");
            detail.setOldValue(product.getProductCompany());
            detail.setNewValue(updatedProduct.getProductCompany());
            product.setProductCompany(updatedProduct.getProductCompany());
            details.add(detail);
        }
        if(product.getProductPrice()!=(updatedProduct.getProductPrice())) {
            flag=true;
            ActivityDetailRequest detail = new ActivityDetailRequest();
            detail.setFieldName("productPrice");
            detail.setOldValue(String.valueOf(product.getProductPrice()));
            detail.setNewValue(String.valueOf(updatedProduct.getProductPrice()));
            product.setProductPrice(updatedProduct.getProductPrice());
            details.add(detail);
        }
        if(flag) {
            log.setAction("UPDATE");
            log.setTableName("PRODUCTS");
            log.setRecordId(product.getProductId());
            log.setUserId(loginedUserId);
            log.setDetails(details);

            User loginedUser = userService.getUserById(loginedUserId);
            activityLogService.addActivityLog(log, loginedUser);
        }
        return productRepo.save(product);
    }

    public void deleteProduct(Integer id,int loginedUserId) {
        ActivityLogRequest log = new ActivityLogRequest();
        log.setAction("DELETE");
        log.setTableName("PRODUCTS");
        log.setRecordId(id);
        log.setUserId(loginedUserId);

        User loginedUser = userService.getUserById(loginedUserId);
        activityLogService.addActivityLog(log, loginedUser);

        productRepo.deleteById(id);
    }

}
