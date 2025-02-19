package com.kdznode.service;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TUser;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author kdz
 * @create 2025-01-23-10:25
 */
public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserPage(Integer current);

    TUser getUserById(Integer id);
}
