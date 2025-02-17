package com.kdznode.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kdz
 * @create 2025-02-05-20:12
 */

@Getter
@AllArgsConstructor
public enum CodeEnum {
    OK(200, "操作成功"),
    FAIL(500, "操作失败"),

    LOGIN_JWT_IS_EMPTY(901,"请求参数为空"),
    LOGIN_JWT_IS_EXPIRE(902,"请求token参数已过期"),
    LOGIN_JWT_NO_MATCH(903,"不匹配"),
    LOGIN_JWT_IS_ILLEGAL(904,"请求token参数不合法")
    ;

    private Integer code;
    private String msg;
}
