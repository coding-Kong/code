package com.kdznode.web;

import com.github.pagehelper.PageInfo;
import com.kdznode.mapper.TActivityMapper;
import com.kdznode.model.TActivity;
import com.kdznode.query.ActivityQuery;
import com.kdznode.result.R;
import com.kdznode.service.ActivityService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author kdz
 * @create 2025-03-04-9:24
 */

@RestController
public class ActivityController {

    @Resource
    private ActivityService activityService;

    /*
     * @description:分页展示以及市场活动动态查询
     * @author: kdz
     * @date: 2025/3/5 23:25
     * @param: [current, activityQuery]
     * @return: com.kdznode.result.R
     **/
    @GetMapping("/api/activitys")
    public R activityPage(@RequestParam(value = "current",required = false)Integer current, ActivityQuery activityQuery){
        if(current == null){
            current = 1;
        }
        PageInfo<TActivity> pageInfo = activityService.getActivityPage(current,activityQuery);
        return R.OK(pageInfo);
    }

    /*
     * @description:落入市场活动
     * @author: kdz
     * @date: 2025/3/5 23:25
     * @param: [activityQuery, token]
     * @return: com.kdznode.result.R
     **/
    @PostMapping("/api/activity")
    public R addActivity(ActivityQuery activityQuery,@RequestHeader(value = "Authorization") String token){
        activityQuery.setToken(token);
        int save = activityService.saveACtivity(activityQuery);
        return save>= 1 ? R.OK() : R.FAIL();
    }
    @GetMapping("/api/activity/{id}")
    public R loadActivity(@PathVariable(value = "id")Integer id){
        TActivity activity = activityService.getActivityById(id);
        return R.OK(activity);
    }

    @PutMapping("/api/activity")
    public R editActivity(ActivityQuery activityQuery,@RequestHeader(value = "Authorization") String token){
        activityQuery.setToken(token);
        int update = activityService.updateACtivity(activityQuery);
        return update>= 1 ? R.OK() : R.FAIL();
    }
}
