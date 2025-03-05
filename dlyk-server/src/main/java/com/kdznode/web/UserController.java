package com.kdznode.web;

import com.github.pagehelper.PageInfo;
import com.kdznode.model.TUser;
import com.kdznode.query.UserQuery;
import com.kdznode.result.R;
import com.kdznode.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

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


    /**
     * @description: 分页查询用户列表
     * @author: kdz
     * @date: 2025/2/25 22:47
     * @param: [current]
     * @return: com.kdznode.result.R
     **/
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

    @GetMapping("api/user/{id}")
    public R UserDetail(@PathVariable(value = "id")Integer id){
        TUser tUser = userService.getUserById(id);
        return R.OK(tUser);
    }

    /**
     * 添加用户
     * @param userQuery
     * @return
     */
    @PostMapping("api/user")
    public R addUser(UserQuery userQuery,@RequestHeader(value = "Authorization") String token ){
        userQuery.setToken(token);
        int save = userService.saveUser(userQuery);
        return save>= 1 ? R.OK() : R.FAIL();
    }

    @GetMapping("api/user/owner")
    public R owner(){
        List<TUser> owners = userService.getOwners();
        return R.OK(owners);
    }

    /**
     * 编辑用户
     * @param userQuery
     * @return
     */
    @PutMapping("api/user")
    public R editUser(UserQuery userQuery,@RequestHeader(value = "Authorization") String token ){
        userQuery.setToken(token);
        int update = userService.updateUser(userQuery);
        return update>= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 编辑用户
     * @param
     * @return
     */
    @DeleteMapping("api/user/{id}")
    public R deleteUser(@PathVariable(value = "id")Integer id ){
        int del = userService.deleteUser(id);
        return del>= 1 ? R.OK() : R.FAIL();
    }

    @DeleteMapping("api/user/batch")
    public R batchdeleteUser(@RequestParam(value = "ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        int batchDel = userService.batchdeleteUser(idList);
        return batchDel>= idList.size() ? R.OK() : R.FAIL();
    }
}
