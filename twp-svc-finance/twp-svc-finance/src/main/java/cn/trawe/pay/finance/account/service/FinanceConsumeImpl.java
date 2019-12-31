package cn.trawe.pay.finance.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.FinanceReq;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;

@Component("CA")
public class FinanceConsumeImpl implements FinanceConsumeInter {
	
	@Autowired
	FinanceApiService FinanceApiService;

	@Override
	public BaseResp consume(JSONObject req) {
		
		return FinanceApiService.consume(req.toJavaObject(AccountConsumeReq.class));
	}

	@Override
	public BaseResp deal(JSONObject req) {
		
		return consume(req);
	}

}
