package com.kdznode.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author kdz
 * @create 2025-01-23-10:15
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class R  {
    private int code;
    private String msg;
    private Object data;


    public static R OK() {
        return R.builder().code(CodeEnum.OK.getCode()).msg(CodeEnum.OK.getMsg()).build();
    }

    public static R OK(int code, String msg) {
        return R.builder().code(code).msg(msg).build();
    }
    public static R OK(Object data) {
        return R.builder().code(CodeEnum.OK.getCode()).msg(CodeEnum.OK.getMsg()).data(data).build();
    }


    public static R FAIL() {
        return R.builder().code(CodeEnum.FAIL.getCode()).msg(CodeEnum.FAIL.getMsg()).build();
    }

    public static R FAIL(String msg) {
        return R.builder().code(CodeEnum.FAIL.getCode()).msg(msg).build();
    }

    public static R FAIL(CodeEnum codeEnum) {
        return R.builder().code(codeEnum.getCode()).msg(codeEnum.getMsg()).build();
    }
}
