package com.kdznode.service;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TActivityRemark;
import com.kdznode.query.ActivityRemarkQuery;

/**
 * @author kdz
 * @create 2025-03-10-22:37
 */
public interface ActivityRemarkService {
    int saveActivityRemark(ActivityRemarkQuery activityRemarkQuery);

    PageInfo<TActivityRemark> getActivityRemarkPage(Integer current,ActivityRemarkQuery activityRemarkQuery);
}
