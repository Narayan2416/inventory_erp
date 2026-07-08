package com.mygroup.inventoryerp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.entity.ActivityLog;
import com.mygroup.inventoryerp.entity.ActivityLogDetail;
import com.mygroup.inventoryerp.repository.ActivityLogDetailRepo;

@Service
public class ActivityLogDetailService {
    
    private final ActivityLogDetailRepo activityLogDetailRepo;

    public ActivityLogDetailService(ActivityLogDetailRepo activityLogDetailRepo){
        this.activityLogDetailRepo=activityLogDetailRepo;
    }

    public List<ActivityLogDetail> getActivityLogDetail(){
        return activityLogDetailRepo.findAll();
    }

    public ActivityLogDetail addActivityLogDetail(ActivityLogRequest log,ActivityLog logRow){
        ActivityLogDetail logDetail=new ActivityLogDetail();
        logDetail.setFieldName(log.getFieldName());
        logDetail.setNewValue(log.getNewValue());
        logDetail.setOldValue(log.getOldValue());

        return activityLogDetailRepo.save(logDetail);
    }

}
