package cn.trawe.pay.finance.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
@Component("RF")
public class FinanceRefundImpl  implements FinanceRefundInter{

	
	@Autowired
	private FinanceApiService FinanceApiService;
	@Override
	public BaseResp deal(JSONObject req) {
		return refund(req);
	}

	@Override
	public BaseResp refund(JSONObject req) {
		return FinanceApiService.refund(req.toJavaObject(AccountRefundReq.class));
	}

}
