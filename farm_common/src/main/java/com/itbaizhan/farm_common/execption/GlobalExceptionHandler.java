package com.itbaizhan.farm_common.execption;

import com.itbaizhan.farm_common.result.BaseResult;
import com.itbaizhan.farm_common.result.CodeEnum;
import lombok.Data;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一处理业务异常和系统异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //系统异常
    @ExceptionHandler(Exception.class)
    public BaseResult defaultHandleException(Exception e){
        e.printStackTrace();
        return BaseResult.error(CodeEnum.SYSTEM_ERROR,e.getMessage());
    }

    //业务异常
    @ExceptionHandler(BusException.class)
    public BaseResult defaultHandleException(BusException e){
        return BaseResult.error(e.getCodeEnum());
    }
}
