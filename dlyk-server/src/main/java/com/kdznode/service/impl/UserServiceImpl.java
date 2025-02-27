package com.kdznode.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kdznode.constant.Constants;
import com.kdznode.mapper.TPermissionMapper;
import com.kdznode.mapper.TRoleMapper;
import com.kdznode.mapper.TUserMapper;
import com.kdznode.model.TPermission;
import com.kdznode.model.TRole;
import com.kdznode.model.TUser;
import com.kdznode.query.BaseQuery;
import com.kdznode.query.UserQuery;
import com.kdznode.service.UserService;
import com.kdznode.util.JWTUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kdz
 * @create 2025-01-23-10:26
 */


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper  tUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TRoleMapper tRoleMapper;

    @Resource
    private TPermissionMapper tPermissionMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = tUserMapper.selectByLoginAct(username);

        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        //查询一下用户的角色
        List<TRole> tRoleList = tRoleMapper.selectByUserId(tUser.getId());
        List<String> roleStringList = new ArrayList<>();

        tRoleList.forEach( tRole -> {
            if (StringUtils.hasText(tRole.getRole())) {
                roleStringList.add(tRole.getRole());
            }
        });
        //查询一下用户的权限
        List<TPermission> tPermissionList = tPermissionMapper.selectByUserId(tUser.getId());
        List<String> permissionStringList = new ArrayList<>();

        tPermissionList.forEach( tPermission -> {
            if (StringUtils.hasText(tPermission.getCode())) {
                permissionStringList.add(tPermission.getCode());
            }
        });

        tUser.setRoleList(roleStringList); //用于数据权限过滤
        tUser.setPermissionList(permissionStringList); //基于权限的权限管理
        return tUser;

    }

    @Override
    public PageInfo<TUser> getUserPage(Integer current) {

        //分页查询三步
        //1.设置分页参数
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询数据
        List<TUser> list =  tUserMapper.selectUsersByPage(BaseQuery.builder().build());
        //3.封装分页数据返回到PageInfo
        PageInfo<TUser> pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public TUser getUserById(Integer id) {
        return tUserMapper.selectUserDetailByID(id);
    }

    @Override
    public int saveUser(UserQuery userQuery) {

        TUser tUser = new TUser();
        BeanUtils.copyProperties(userQuery,tUser);//要求两个对象的属性名以及属性类型要一致
        tUser.setLoginPwd(passwordEncoder.encode(tUser.getLoginPwd()));//加密密码
        tUser.setCreateTime(new Date());
        Integer userId = JWTUtils.parseJWTByUserId(userQuery.getToken());
        tUser.setCreateBy(userId);
        return  tUserMapper.insertSelective(tUser);
    }

    @Override
    public int updateUser(UserQuery userQuery) {
        TUser tUser = new TUser();
        BeanUtils.copyProperties(userQuery,tUser);//要求两个对象的属性名以及属性类型要一致
        if(StringUtils.hasText(userQuery.getLoginPwd())){
            tUser.setLoginPwd(passwordEncoder.encode(tUser.getLoginPwd()));//加密密码
        }
        tUser.setEditTime(new Date());//编辑时间
        Integer userId = JWTUtils.parseJWTByUserId(userQuery.getToken());
        tUser.setEditBy(userId);
        return  tUserMapper.updateByPrimaryKeySelective(tUser);

    }

    @Override
    public int deleteUser(Integer id) {
        return tUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchdeleteUser(List<String> idList) {
        return tUserMapper.deleteByIds(idList);
    }
}

