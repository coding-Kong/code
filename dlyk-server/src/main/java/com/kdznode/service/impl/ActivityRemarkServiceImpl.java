package com.kdznode.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kdznode.constant.Constants;
import com.kdznode.mapper.TActivityRemarkMapper;
import com.kdznode.model.TActivity;
import com.kdznode.model.TActivityRemark;
import com.kdznode.query.ActivityRemarkQuery;
import com.kdznode.query.BaseQuery;
import com.kdznode.service.ActivityRemarkService;
import com.kdznode.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author kdz
 * @create 2025-03-10-22:39
 */

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Resource
    private TActivityRemarkMapper tActivityRemarkMapper;
    @Override
    public int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery) {
        TActivityRemark tActivityRemark = new TActivityRemark();
        BeanUtils.copyProperties(activityRemarkQuery,tActivityRemark);
        tActivityRemark.setCreateTime(new Date());
        Integer userId = JWTUtils.parseJWTByUserId(activityRemarkQuery.getToken());
        tActivityRemark.setCreateBy(userId);
        return tActivityRemarkMapper.insertSelective(tActivityRemark);
    }

    @Override
    public PageInfo<TActivityRemark> getActivityRemarkPage(Integer current,ActivityRemarkQuery activityRemarkQuery) {
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        List<TActivityRemark> list =tActivityRemarkMapper.selectActivityRemarkPage(activityRemarkQuery);
        PageInfo<TActivityRemark> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
