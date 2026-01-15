package com.itbaizhan.farm_common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    /**
     * 枚举之间用逗号隔开
     */
    //成功
    SUCCESS(200,"操作成功"),
    //客户端错误
    NOT_FOUND(404,"资源不存在"),

    /**
     * 增加异常
     */
    //系统异常
    SYSTEM_ERROR(400,"系统异常"),
    //业务异常
    TEST_ERROR(601,"测试业务异常"),
    //用户已经存在
    SYS_USER_EXIST(602,"用户已存在"),
    //角色已经存在
    SYS_ROLE_EXIST(603,"角色已经存在")

    ;

    /**
     * 成员变量必须是private final
     */
    private final Integer code;
    private final String message;


}
