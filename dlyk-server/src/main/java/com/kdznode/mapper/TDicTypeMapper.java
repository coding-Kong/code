package com.kdznode.mapper;

import com.kdznode.model.TDicType;

public interface TDicTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDicType record);

    int insertSelective(TDicType record);

    TDicType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDicType record);

    int updateByPrimaryKey(TDicType record);
}