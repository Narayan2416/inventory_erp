package com.mygroup.inventoryerp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityDetailRequest;
import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.entity.ActivityLog;
import com.mygroup.inventoryerp.entity.ActivityLogDetail;
import com.mygroup.inventoryerp.entity.User;
import com.mygroup.inventoryerp.repository.ActivityLogRepo;

@Service
public class ActivityLogService {
    private ActivityLogRepo activityLogRepo;

    public ActivityLogService(ActivityLogRepo activityLogRepo){
        this.activityLogRepo=activityLogRepo;
    }

    public List<ActivityLog> getActivityLog(){
        return activityLogRepo.findAll();
    }

    public ActivityLog addActivityLog(ActivityLogRequest log,User loginedUser){
        ActivityLog row=new ActivityLog();

        row.setAction(log.getAction());
        row.setActivityTime();
        row.setRecordId(log.getRecordId());
        row.setTableName(log.getTableName());

        row.setUser(loginedUser);
        if(log.getAction().equals("UPDATE")){
            List<ActivityLogDetail> details = new ArrayList<>();
            for(ActivityDetailRequest detail: log.getDetails()){
                ActivityLogDetail logDetail = new ActivityLogDetail();
                logDetail.setFieldName(detail.getFieldName());
                logDetail.setNewValue(detail.getNewValue());
                logDetail.setOldValue(detail.getOldValue());
                logDetail.setActivityLog(row);
                details.add(logDetail);
            }
            row.setActivityLogDetail(details);
        }

        return activityLogRepo.save(row);
    }

}
