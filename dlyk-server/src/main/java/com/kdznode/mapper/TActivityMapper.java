package com.kdznode.mapper;

import com.kdznode.commons.DataScope;
import com.kdznode.model.TActivity;
import com.kdznode.query.ActivityQuery;
import com.kdznode.query.BaseQuery;

import java.util.List;

public interface TActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    @DataScope(tableAlias = "ta", tableField = "owner_id")
    List<TActivity> selectByActivityPage(ActivityQuery activityQuery);

    TActivity selectDetailByPrimaryKey(Integer id);
}