package com.kdznode.config.handler;

import com.kdznode.constant.Constants;
import com.kdznode.model.TUser;
import com.kdznode.result.R;
import com.kdznode.service.RedisService;
import com.kdznode.util.JSONUtils;
import com.kdznode.util.JWTUtils;
import com.kdznode.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author kdz
 * @create 2025-01-23-12:01
 */

@Component
public class MySucessfulHandler implements AuthenticationSuccessHandler {


    @Resource
    private RedisService redisService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //由于禁用了session，我们在登录成功后，需要在服务器保持用户的登录状态，前端下次来访问服务器端的时候，服务器端要知道这个人登录了
        TUser tUser = (TUser) authentication.getPrincipal();

        //生成jwt字符串
        String userJson = JSONUtils.toJSON(tUser);
        String jwt = JWTUtils.createJWT(userJson);

        //将jwt字符串写入redis
        Integer userId = tUser.getId();
        redisService.setValue(Constants.REDIS_JWT_KEY + userId,jwt);
        //设置过期时间 选择记住我是7天过期，否则是30分钟过期
        String rememberMe = request.getParameter("rememberMe"); //true，false，undefined
        if (Boolean.parseBoolean(rememberMe)) {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.EXPIRE_TIME, TimeUnit.MINUTES);
        } else {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
        }

        R result = R.OK(jwt);

        String resultJson = JSONUtils.toJSON(result);
        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJson);

    }
}
