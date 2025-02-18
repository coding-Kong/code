package com.kdznode.web;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TUser;
import com.kdznode.result.R;
import com.kdznode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kdz
 * @create 2025-02-05-23:34
 */

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/api/login/info")
    public R loginInfo(Authentication authentication){
        TUser tUser = (TUser)authentication.getPrincipal();
        return R.OK(tUser);
    }

    @GetMapping("api/login/free")
    public R freeLogin(){
        return R.OK();
    }

    public Authentication getAuthentication(){
        return null;
    }


    @GetMapping("api/users")
    public R userPage(@RequestParam(value = "current",required = false) Integer current){

        //required = false 表示这个参数不是必须的
        //required = true 表示这个参数是必须的,不传会报异常
        if(current==null){
            current = 1;
        }
        PageInfo<TUser> pageInfo = userService.getUserPage(current);
        return R.OK(pageInfo);
    }
}
