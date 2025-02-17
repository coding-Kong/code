package com.kdznode.mapper;

import com.kdznode.model.TTranRemark;

public interface TTranRemarkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTranRemark record);

    int insertSelective(TTranRemark record);

    TTranRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTranRemark record);

    int updateByPrimaryKey(TTranRemark record);
}