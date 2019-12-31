package cn.trawe.pay.finance.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountRechargeReq;
@Component("RA")
public class FinanceRechargeImpl implements FinanceRechargeInter{
	
	
	@Autowired
	private FinanceApiService  FinanceApiService;
	
	

	@Override
	public BaseResp deal(JSONObject req) {
		
		return recharge(req);
	}

	@Override
	public BaseResp recharge(JSONObject req) {
		
		return FinanceApiService.recharge(req.toJavaObject(AccountRechargeReq.class));
	}

}
