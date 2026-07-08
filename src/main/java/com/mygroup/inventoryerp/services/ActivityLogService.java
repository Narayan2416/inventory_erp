package com.mygroup.inventoryerp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.entity.ActivityLog;
import com.mygroup.inventoryerp.entity.User;
import com.mygroup.inventoryerp.repository.ActivityLogRepo;

@Service
public class ActivityLogService {
    private ActivityLogRepo activityLogRepo;
    private ActivityLogDetailService activityLogDetailService;

    public ActivityLogService(ActivityLogDetailService activityLogDetailService,ActivityLogRepo activityLogRepo){
        this.activityLogRepo=activityLogRepo;
        this.activityLogDetailService=activityLogDetailService;
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

        ActivityLog logRow=activityLogRepo.save(row);
        if(!logRow.getAction().equals("UPDATE")) return logRow;

        activityLogDetailService.addActivityLogDetail(log,logRow);
        return logRow;
    }

}
