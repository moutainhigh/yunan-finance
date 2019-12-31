package cn.trawe.pay.finance.account.service;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;

public interface FinanceConsumeInter extends FinanceInterface {
	
	public BaseResp consume(JSONObject req);

}
