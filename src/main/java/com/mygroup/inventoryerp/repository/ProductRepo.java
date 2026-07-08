package com.mygroup.inventoryerp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mygroup.inventoryerp.entity.Product;

public interface ProductRepo extends JpaRepository<Product,Integer>{
    Optional<Product> findByProductNameAndProductCompany(String productName,String productCompany);

    @Query("select distinct p.productCompany from Product p")
    List<String> findAllCompanies();

    @Query("select distinct p.productType from Product p")
    List<String> findAllProductType();
}
