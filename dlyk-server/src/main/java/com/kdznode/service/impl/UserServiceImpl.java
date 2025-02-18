package com.kdznode.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kdznode.constant.Constants;
import com.kdznode.mapper.TUserMapper;
import com.kdznode.model.TUser;
import com.kdznode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kdz
 * @create 2025-01-23-10:26
 */


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TUserMapper  tUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = tUserMapper.selectByLoginAct(username);

        if(tUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }

        return tUser;

    }

    @Override
    public PageInfo<TUser> getUserPage(Integer current) {

        //分页查询三步
        //1.设置分页参数
        PageHelper.startPage(current, Constants.PAGE_SIZE);
        //2.查询数据
        List<TUser> list =  tUserMapper.selectUsersByPage();
        //3.封装分页数据返回到PageInfo
        PageInfo<TUser> pageInfo = new PageInfo(list);
        return pageInfo;
    }
}

