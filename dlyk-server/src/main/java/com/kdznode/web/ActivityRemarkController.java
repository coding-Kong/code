package com.kdznode.web;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TActivityRemark;
import com.kdznode.query.ActivityRemarkQuery;
import com.kdznode.result.R;
import com.kdznode.service.ActivityRemarkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author kdz
 * @create 2025-03-10-22:30
 */

@RestController
public class ActivityRemarkController {

    @Resource
    private ActivityRemarkService activityRemarkService;

    @PostMapping("/api/activity/remark")
    public R addActivityRemark(@RequestBody ActivityRemarkQuery activityRemarkQuery, @RequestHeader(value = "Authorization") String token){

        activityRemarkQuery.setToken(token);
        int save = activityRemarkService.saveActivityRemark(activityRemarkQuery);
        return  save >= 0 ? R.OK() : R.FAIL();
    }

    @GetMapping("/api/activity/remark")
    public R activityRemarkPage(@RequestParam(value = "current",required = false) Integer current,
                                @RequestParam(value = "activityId") Integer activityId){

        if(current == null){
            current =1;
        }
        ActivityRemarkQuery activityRemarkQuery = new ActivityRemarkQuery();
        activityRemarkQuery.setActivityId(activityId);
        PageInfo<TActivityRemark> pageInfo = activityRemarkService.getActivityRemarkPage(current,activityRemarkQuery);
        return  R.OK(pageInfo);
    }
}
