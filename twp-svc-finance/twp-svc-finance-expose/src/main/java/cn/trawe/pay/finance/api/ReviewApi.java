package cn.trawe.pay.finance.api;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.ReviewReq;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 审核接口
 *
 * @author liguangyan
 * @date 2019/12/5 14:29
 */
@RequestMapping("/review")
public interface ReviewApi {
    /**
     * 审核商户结算账单
     *
     * @return
     */
    @PostMapping("/mch/settle/bill")
    BaseResp reviewMchSettleBill(@RequestBody ReviewReq reviewReq);
}
