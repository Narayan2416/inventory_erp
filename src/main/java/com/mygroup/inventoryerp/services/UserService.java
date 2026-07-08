package com.mygroup.inventoryerp.services;

import java.util.*;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.dto.LoginRequest;
import com.mygroup.inventoryerp.dto.UserInfo;
import com.mygroup.inventoryerp.entity.Role;
import com.mygroup.inventoryerp.entity.User;
import com.mygroup.inventoryerp.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;


@Service
public class UserService {

    private final ActivityLogService activityLogService;
    private final UserRepo userRepo;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    

    public UserService(UserRepo userRepo, RoleService roleService, PasswordEncoder passwordEncoder, ActivityLogService activityLogService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.activityLogService = activityLogService;
    }

    public Map<String,Object> login(LoginRequest loginRequest) {
        Optional<User> user =userRepo.findByUserName(loginRequest.getUserName());
        Map<String,Object> response = new HashMap<>();

        if(user.isEmpty()) {
            response.put("message", "User does not exist");
            response.put("valid", false);
            return response;
        }

        User foundUser = user.get();
        if(passwordEncoder.matches(loginRequest.getPassword(), foundUser.getPassword())) {
            foundUser.setLastLogin();
            userRepo.save(foundUser);
            response.put("message", "Login successful");
            response.put("valid", true);
            response.put("userName", foundUser.getUserName());
            response.put("userId",foundUser.getUserId());
            response.put("roleId", foundUser.getRole().getRoleId());
            //System.out.println(foundUser.getRole().getRoleId());
            return response;
        }

        response.put("message", "Invalid password");
        response.put("valid", false);
        return response;

    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public Map<String,Object> addUser(UserInfo userInfo,HttpSession session) {
        ActivityLogRequest log=new ActivityLogRequest();
        log.setAction("CREATE");
        log.setTableName("USERS");
        log.setUserId((Integer)session.getAttribute("userId"));

        Optional<User> existingUser = userRepo.findByUserName(userInfo.getUserName().toUpperCase());
        Map<String,Object> response = new HashMap<>();
        if (existingUser.isPresent()) {
            response.put("message", "User already exists");
            response.put("valid", false);
            return response;
        }

        User user=new User();
        user.setUserEmail(userInfo.getUserEmail());
        user.setUserName(userInfo.getUserName());
        user.setPassword(passwordEncoder.encode((String) userInfo.getPassword()));
        Role role = roleService.getRoleById(userInfo.getRoleId());
        user.setRole(role);
        user.setCreatedAt();
        user.getLastLogin();

        User addedUser=userRepo.save(user);
       // User loginedUser=userRepo.findById((Integer) session.getAttribute("userId")).orElse(null);

        log.setRecordId(addedUser.getUserId());
        //activityLogService.addActivityLog(log,loginedUser);

        response.put("message", "User created successfully");
        response.put("valid", true);
        return response;
    }

    public User editUser(Integer id, UserInfo userInfo,HttpSession session) {

        User user = userRepo.findById(id).orElse(null);
        if(user==null){
            return null;
        }

        ActivityLogRequest log=new ActivityLogRequest();
        log.setAction("UPDATE");
        log.setTableName("USERS");
        log.setUserId((Integer)session.getAttribute("userId"));
        log.setRecordId(id);

        if(!userInfo.getPassword().isEmpty()) user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        Role role=roleService.getRoleById(userInfo.getRoleId());
        user.setRole(role);

        user.setUserName(userInfo.getUserName());
        user.setUserEmail(userInfo.getUserEmail());

        User addedUser= userRepo.save(user);
        //User loginedUser=userRepo.findById((Integer)session.getAttribute("userId")).orElse(null);

        log.setRecordId(addedUser.getUserId());
        
        //activityLogService.addActivityLog(log,loginedUser);
        return addedUser;
    }

    public void deleteUser(int id) {
        userRepo.deleteById(id);
    }
}