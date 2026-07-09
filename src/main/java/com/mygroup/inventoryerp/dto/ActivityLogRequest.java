package com.mygroup.inventoryerp.dto;

import java.util.List;

public class ActivityLogRequest {

    private String action;
    private Integer recordId;
    private String tableName;
    private Integer userId;

    List<ActivityDetailRequest> details;

    // Getter and Setter for action
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Getter and Setter for recordId
    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    // Getter and Setter for tableName
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    // Getter and Setter for userId
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<ActivityDetailRequest> getDetails() {
        return details;
    }

    public void setDetails(List<ActivityDetailRequest> details) {
        this.details = details;
    }
}