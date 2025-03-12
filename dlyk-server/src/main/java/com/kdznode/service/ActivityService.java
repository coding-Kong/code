package com.kdznode.service;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TActivity;
import com.kdznode.query.ActivityQuery;

/**
 * @author kdz
 * @create 2025-03-04-9:30
 */
public interface ActivityService {
    PageInfo<TActivity> getActivityPage(Integer current, ActivityQuery activityQuery);

    int saveACtivity(ActivityQuery activityQuery);

    TActivity getActivityById(Integer id);

    int updateACtivity(ActivityQuery activityQuery);
}
