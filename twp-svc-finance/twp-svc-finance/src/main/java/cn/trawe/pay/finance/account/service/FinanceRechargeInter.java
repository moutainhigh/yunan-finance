package cn.trawe.pay.finance.account.service;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;

public interface FinanceRechargeInter  extends FinanceInterface {
	
	public BaseResp recharge(JSONObject req);

}
