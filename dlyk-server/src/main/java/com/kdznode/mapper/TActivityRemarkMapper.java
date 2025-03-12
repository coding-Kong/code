package com.kdznode.mapper;

import com.kdznode.commons.DataScope;
import com.kdznode.model.TActivityRemark;
import com.kdznode.query.ActivityRemarkQuery;
import com.kdznode.query.BaseQuery;

import java.util.List;

public interface TActivityRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivityRemark record);

    int insertSelective(TActivityRemark record);

    TActivityRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivityRemark record);

    int updateByPrimaryKey(TActivityRemark record);

    @DataScope(tableAlias = "tar", tableField = "create_by")
    List<TActivityRemark> selectActivityRemarkPage(ActivityRemarkQuery activityRemarkQuery);
}