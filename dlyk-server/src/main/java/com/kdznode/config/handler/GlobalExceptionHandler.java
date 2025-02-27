package com.kdznode.config.handler;

import com.kdznode.result.CodeEnum;
import com.kdznode.result.R;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    DataIntegrityViolationException e;

    @ExceptionHandler(value = Exception.class)
    public R handlerException(Exception e) {
        e.printStackTrace();
        return R.FAIL(e.getMessage());
    }

    /**
     * 数据库操作异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = DataAccessException.class)
    public R DataAccessException(DataAccessException e){
        e.printStackTrace();
        return R.FAIL(CodeEnum.DATA_ACCESS_EXCEPTION);
    }

}
