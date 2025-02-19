package com.kdznode.mapper;

import com.kdznode.model.TUser;
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

    List<TUser> selectUsersByPage();

    TUser selectUserDetailByID(Integer id);
}