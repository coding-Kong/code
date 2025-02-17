package com.kdznode.service.impl;

import com.kdznode.mapper.TUserMapper;
import com.kdznode.model.TUser;
import com.kdznode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}

