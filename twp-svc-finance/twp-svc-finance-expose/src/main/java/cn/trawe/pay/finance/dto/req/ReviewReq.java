package cn.trawe.pay.finance.dto.req;

import cn.trawe.pay.finance.dto.BaseReq;
import lombok.Data;

/**
 * 审核
 *
 * @author liguangyan
 * @date 2019/12/5 14:35
 */
@Data
public class ReviewReq extends BaseReq {
    /**
     * 商户账单批次号
     */
    private String batchNo;

    /**
     * 审核结果
     * 账单状态（0：已完成 1：后台人员处理中 3.后台人员审核不通过 4.财务人员审核中（后台人员审核通过）5.财务人员审核通过，6.财务人员审核不通过）
     */
    private String reviewResult;

    /**
     * 审核人
     */
    private String updateOperator;

}
