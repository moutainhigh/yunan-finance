package cn.trawe.pay.finance.task.controller;

import cn.trawe.pay.finance.api.ReviewApi;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.ReviewReq;
import cn.trawe.pay.finance.task.service.SettleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liguangyan
 * @date 2019/12/5 15:06
 */
@RestController
@Slf4j
public class ReviewController implements ReviewApi {
    @Autowired
    private SettleService settleService;

    @Override
    public BaseResp reviewMchSettleBill(ReviewReq reviewReq) {
        return settleService.reviewMchSettleBill(reviewReq);
    }
}
