package com.itbaizhan.farm_common.execption;

import com.itbaizhan.farm_common.result.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 统一处理异常
 */
@Data
@AllArgsConstructor
public class BusException extends RuntimeException{
    private CodeEnum codeEnum;
}
