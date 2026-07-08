package com.mygroup.inventoryerp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.entity.Role;
import com.mygroup.inventoryerp.repository.RoleRepo;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Role getRoleById(Integer id) {
        return roleRepo.findById(id).orElse(null);
    }

    public Role addRole(Role role) {
        return roleRepo.save(role);
    }

    public void deleteRole(Integer id) {
        roleRepo.deleteById(id);
    }
}
