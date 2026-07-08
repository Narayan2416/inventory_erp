package com.mygroup.inventoryerp.dto;

public class ActivityLogRequest {

    private String action;
    private Integer recordId;
    private String tableName;
    private Integer userId;

    private String fieldName;
    private String oldValue;
    private String newValue;

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

    // Getter and Setter for fieldName
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    // Getter and Setter for oldValue
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    // Getter and Setter for newValue
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}