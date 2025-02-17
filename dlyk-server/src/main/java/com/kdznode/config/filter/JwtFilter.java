package com.kdznode.config.filter;


import com.kdznode.constant.Constants;
import com.kdznode.model.TUser;
import com.kdznode.result.CodeEnum;
import com.kdznode.result.R;
import com.kdznode.service.RedisService;
import com.kdznode.util.JSONUtils;
import com.kdznode.util.JWTUtils;
import com.kdznode.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals(Constants.LOGIN_URI)) {
            //登录的时候还没有生成jwt，此时不需要验证jwt，直接放行即可，可以让filter链继续执行，执行下一个filter
            filterChain.doFilter(request, response);

        } else {
            //拿到前端传过来的jwt（token），这个jwt一般都通过header传过来
            String jwt = null;
            if (requestURI.equals(Constants.EXPORT_EXCEL_URI)) {//如果是导出Excel的话，token是在地址栏中
                jwt = request.getParameter(Constants.TOKEN_NAME);
            } else {
                jwt = request.getHeader(Constants.TOKEN_NAME);
            }

            if (!StringUtils.hasText(jwt)) {
                //jwt是空的，那不合法
                //执行到这里，那我们向前端返回json就行了
                R result = R.FAIL(CodeEnum.LOGIN_JWT_IS_EMPTY);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //jwt不是空，但是验证未通过（比如jwt被篡改过）
            if (!JWTUtils.verifyJWT(jwt)) {
                //执行到这里，那我们向前端返回json就行了
                R result = R.FAIL(CodeEnum.LOGIN_JWT_IS_ILLEGAL);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //怎么拿到用户的id？反写解析jwt，从jwt中解析出用户的id
            String userJSON = JWTUtils.parseJWT(jwt);
            TUser tUser = JSONUtils.toBean(userJSON, TUser.class);
            Integer userId = tUser.getId();
            String redisJWT = (String) redisService.getValue(Constants.REDIS_JWT_KEY + userId);
            if (!StringUtils.hasText(redisJWT)) {
                //jwt过期了
                //执行到这里，那我们向前端返回json就行了
                R result = R.FAIL(CodeEnum.LOGIN_JWT_IS_EXPIRE);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //redis中有jwt，此时就比较前端传过来的jwt和redis的jwt是否相等
            if (!jwt.equals(redisJWT)) {
                //执行到这里，那我们向前端返回json就行了
                R result = R.FAIL(CodeEnum.LOGIN_JWT_NO_MATCH);

                //把R对象转成json
                String resultJSON = JSONUtils.toJSON(result);

                //把json写出去，写到浏览器
                ResponseUtils.write(response, resultJSON);
                return;
            }

            //给token续期
            String rememberMe = request.getHeader(Constants.REMEMBERME_NAME); //true, null
            if (Boolean.parseBoolean(rememberMe)) {
                //续期7天
                redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.EXPIRE_TIME, TimeUnit.MINUTES);
            } else {
                //续期30分钟
                redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.DEFAULT_EXPIRE_TIME, TimeUnit.MINUTES);
            }

            //都验证通过了，没有问题了，需要告诉spring security框架，这样spring security框架才知道该jwt是已经登录过的
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(tUser, tUser.getPassword(), tUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            //下面就是filter链继续执行，执行下一个filter
            filterChain.doFilter(request, response);
        }
    }
}
