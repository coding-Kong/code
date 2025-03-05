package com.kdznode.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kdznode.constant.Constants;
import com.kdznode.mapper.TActivityMapper;
import com.kdznode.model.TActivity;
import com.kdznode.model.TUser;
import com.kdznode.query.ActivityQuery;
import com.kdznode.query.BaseQuery;
import com.kdznode.service.ActivityService;
import com.kdznode.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author kdz
 * @create 2025-03-04-9:32
 */

@Service
public class ActivityServiceImpl implements ActivityService {


    @Resource
    private TActivityMapper tActivityMapper;
    @Override
    public PageInfo<TActivity> getActivityPage(Integer current, ActivityQuery activityQuery) {
        //分页查询三行代码
        PageHelper.startPage(current, Constants.PAGE_SIZE);//当前页码，以及每页条数
        List<TActivity> list =  tActivityMapper.selectByActivityPage(activityQuery);
        PageInfo<TActivity> info = new PageInfo<>(list);
        return info;
    }

    @Override
    public int saveACtivity(ActivityQuery activityQuery) {
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(activityQuery,tActivity);//要求两个对象的属性名以及属性类型要一致
        tActivity.setCreateTime(new Date());
        Integer userId = JWTUtils.parseJWTByUserId(activityQuery.getToken());
        tActivity.setCreateBy(userId);
        return  tActivityMapper.insertSelective(tActivity);
    }
}
