package cn.trawe.pay.finance.enums;

import lombok.Getter;

/**
 * @author liguangyan
 * @date 2019/12/5 14:32
 */
@Getter
public enum ErrorCodeEnum {
    SUCCESS(0, "成功"),
    FAIL(0, "失败");
    private int code;
    private String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
