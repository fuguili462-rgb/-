package com.itbaizhan.farm_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
//通用模块
/**
 * 这是一个专门统一返回结果格式的实体类
 * 后端给前端传递的数据数据统一为json格式的数据
 */
@Data
@AllArgsConstructor
public class BaseResult<T> {
    //状态码
    private Integer code;
    //提示信息
    private String message;
    //返回具体数据
    private T data;

    //构建带有数据的成功结果
    public static <T> BaseResult<T> ok(T data) {
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(),data);
    }

    //构建成功结果
    public static <T> BaseResult<T> ok() {
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(),CodeEnum.SUCCESS.getMessage(), null);
    }

    //构建失败返回的结果
    public static <T> BaseResult<T> error(CodeEnum codeEnum, T data) {
        return new BaseResult<>(codeEnum.getCode(),codeEnum.getMessage(),data);
    }

    //
    public static <T> BaseResult<T> error(CodeEnum codeEnum) {
        return new BaseResult<>(codeEnum.getCode(),codeEnum.getMessage(),null);
    }
}
