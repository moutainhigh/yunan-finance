package cn.trawe.pay.finance.enums;

import lombok.Getter;

/**
 * 结算批次状态
 *
 * @author liguangyan
 * @date 2019/11/29 16:17
 */
@Getter
public enum SettleBatchStatusEnum {
    INIT(0, "初始状态"),
    SUCCESS(1, "成功"),
    COMPARE_BILL_FAIL(2, "对账失败"),
    UPDATE_FAIL(3, "更新失败");

    private int status;
    private String name;

    SettleBatchStatusEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }
}
