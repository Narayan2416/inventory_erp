package com.mygroup.inventoryerp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="activity_log_details")
public class ActivityLogDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="detail_id")
    private int detailId;

    @Column(name = "field_name",nullable = false)
    private String fieldName;

    @Column(name = "old_value",nullable = false)
    private String oldValue;

    @Column(name = "new_value",nullable = false)
    private String newValue;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "log_id", nullable = false)
    private ActivityLog activityLog;

    public int getDetailId(){
        return detailId;
    }

    public void setDetailId(int detailId){
        this.detailId=detailId;
    }

    public String getFieldName(){
        return fieldName;
    }

    public void setFieldName(String fielfName){
        this.fieldName=fielfName;
    }

    public String getOldValue(){
        return oldValue;
    }

    public void setOldValue(String oldValue){
        this.oldValue=oldValue;
    }

    public String getNewValue(){
        return newValue;
    }

    public void setNewValue(String newValue){
        this.newValue=newValue;
    }

}
