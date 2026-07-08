/*package com.mygroup.inventoryerp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mygroup.inventoryerp.dto.ActivityDetailRequest;
import com.mygroup.inventoryerp.dto.ActivityLogRequest;
import com.mygroup.inventoryerp.entity.ActivityLog;
import com.mygroup.inventoryerp.entity.ActivityLogDetail;
import com.mygroup.inventoryerp.repository.ActivityLogDetailRepo;

public class ActivityLogDetailService {
    
    private final ActivityLogDetailRepo activityLogDetailRepo;

    public ActivityLogDetailService(ActivityLogDetailRepo activityLogDetailRepo){
        this.activityLogDetailRepo=activityLogDetailRepo;
    }

    public List<ActivityLogDetail> getActivityLogDetail(){
        return activityLogDetailRepo.findAll();
    }

    public List<ActivityLogDetail> addActivityLogDetail(ActivityLogRequest log,ActivityLog logRow){
        List<ActivityLogDetail> logDetails = new ArrayList<>();
        for(ActivityDetailRequest detail : log.getDetails()){
            ActivityLogDetail logDetail = new ActivityLogDetail();
            logDetail.setFieldName(detail.getFieldName());
            logDetail.setNewValue(detail.getNewValue());
            logDetail.setOldValue(detail.getOldValue());
            logDetail.setActivityLog(logRow);
            logDetails.add(logDetail);
        }
        return activityLogDetailRepo.saveAll(logDetails);
    }

        return activityLogDetailRepo.save(logDetail);
    }

}
*/