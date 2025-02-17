package com.kdznode.web;

import com.kdznode.model.TUser;
import com.kdznode.result.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kdz
 * @create 2025-02-05-23:34
 */

@RestController
public class UserController {


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
}
