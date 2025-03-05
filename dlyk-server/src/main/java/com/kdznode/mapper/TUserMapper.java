package com.kdznode.mapper;

import com.kdznode.commons.DataScope;
import com.kdznode.model.TUser;
import com.kdznode.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(@Param("username")  String username);


    @DataScope(tableAlias = "tu", tableField = "id")
    List<TUser> selectUsersByPage(BaseQuery query);

    TUser selectUserDetailByID(Integer id);

    int deleteByIds(List<String> idList);

    List<TUser> selectUserOwner();
}