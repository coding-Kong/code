package com.kdznode.service;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TUser;
import com.kdznode.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author kdz
 * @create 2025-01-23-10:25
 */
public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUserPage(Integer current);

    TUser getUserById(Integer id);

    int saveUser(UserQuery userQuery);

    int updateUser(UserQuery userQuery);

    int deleteUser(Integer id);

    int batchdeleteUser(List<String> idList);
}
