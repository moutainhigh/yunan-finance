package cn.trawe.pay.finance.enums;

import lombok.Getter;

/**
 * 商户结算账单表，账单状态
 *
 * @author liguangyan
 * @date 2019/12/4 11:04
 */
@Getter
public enum MchSettleBillStatusEnum {
    //    账单状态（0：已完成 1：后台人员处理中 3.后台人员审核不通过 4.财务人员审核中（后台人员审核通过）5.财务人员审核通过，6.财务人员审核不通过）
    COMPLETED("0", "已完成"),
    BACKGROUND_PROCESSING("1", "后台人员处理中"),
    BACKGROUND_REFUSE("3", "后台人员审核不通过"),
    FINANCE_PROCESSING("4", "财务人员审核中"),
    FINANCE_PASS("5", "财务人员审核通过"),
    FINANCE_REFUSE("6", "财务人员审核不通过");

    private String status;
    private String name;

    MchSettleBillStatusEnum(String status, String name) {
        this.status = status;
        this.name = name;
    }
}
