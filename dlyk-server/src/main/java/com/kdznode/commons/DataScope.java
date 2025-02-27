package com.kdznode.commons;

import java.lang.annotation.*;

/**
 * @author kdz
 * @create 2025-02-25-22:37
 * 数据范围注解
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {



    /**
     * 表的别名
     */
    public String tableAlias() default "";

    /**
     * 表的字段名
     */
    public String tableField() default "";
}
