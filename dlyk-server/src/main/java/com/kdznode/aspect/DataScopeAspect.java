package com.kdznode.aspect;

import com.kdznode.commons.DataScope;
import com.kdznode.constant.Constants;
import com.kdznode.query.BaseQuery;
import com.kdznode.util.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

/**
 * @author kdz
 * @create 2025-02-25-22:58
 */

@Component
@Aspect
public class DataScopeAspect {


    /**
     * @description:设计切入点
     * @author: kdz
     * @date: 2025/2/25 23:05
     * @param: []
     * @return: void
     **/
    @Pointcut(value = "@annotation(com.kdznode.commons.DataScope)")
    private void pointcut(){
    }

    @Around(value = "pointcut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {


        //通过joinPoint获取切入的方法，包括方法注解，形参
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        DataScope dataScope = signature.getMethod().getAnnotation(DataScope.class);
        String tableAlias = dataScope.tableAlias();//表别名
        String tableField = dataScope.tableField();//表字段名

        //通过request 获取用户token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.TOKEN_NAME);
        //获取用户角色和id
        List<String> roleList = JWTUtils.parseJWTByUserRole(token);
        Integer userId = JWTUtils.parseJWTByUserId(token);

        if(!roleList.contains("admin")){
            Object paras = joinPoint.getArgs()[0];//获取第一个参数,即要拼的参数
            if(paras instanceof BaseQuery){
                BaseQuery baseQuery = (BaseQuery) paras;
                //and where tu.id = 2;
                baseQuery.setFilterSQL(" AND " + tableAlias + "." + tableField + " = " +userId);
            }
        }
        System.out.println("调用目标方法前................" + new Date());
        //执行被拦截的方法
        Object result= joinPoint.proceed();
        System.out.println("调用目标方法后................" + new Date() + ", result = " + result);
        return result;
    }

}
