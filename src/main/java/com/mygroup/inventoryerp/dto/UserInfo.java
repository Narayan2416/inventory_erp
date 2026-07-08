package com.mygroup.inventoryerp.dto;

public class UserInfo {
    private String userName;
    private String userEmail;
    private int roleId;
    private String password;

    public UserInfo(){

    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail=userEmail;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public int getRoleId(){
        return roleId;
    }

    public void setRoleId(int roleId){
        this.roleId=roleId;
    }

}
