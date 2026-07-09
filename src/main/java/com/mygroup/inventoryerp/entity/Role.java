package com.mygroup.inventoryerp.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "users_access")
    private String usersAccess;

    @Column(name = "customers_access")
    private String customersAccess;

    @Column(name = "products_access")
    private String productsAccess;

    @Column(name = "orders_access")
    private String ordersAccess;

    @Column(name = "inventory_access")
    private String inventoryAccess;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<User> users;

    public Role() {
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUsersAccess() {
        return usersAccess;
    }

    public void setUsersAccess(String usersAccess) {
        this.usersAccess = usersAccess;
    }

    public String getCustomersAccess() {
        return customersAccess;
    }

    public void setCustomersAccess(String customersAccess) {
        this.customersAccess = customersAccess;
    }

    public String getProductsAccess() {
        return productsAccess;
    }

    public void setProductsAccess(String productsAccess) {
        this.productsAccess = productsAccess;
    }

    public String getOrdersAccess() {
        return ordersAccess;
    }

    public void setOrdersAccess(String ordersAccess) {
        this.ordersAccess = ordersAccess;
    }

    public String getInventoryAccess() {
        return inventoryAccess;
    }

    public void setInventoryAccess(String inventoryAccess) {
        this.inventoryAccess = inventoryAccess;
    }
}
