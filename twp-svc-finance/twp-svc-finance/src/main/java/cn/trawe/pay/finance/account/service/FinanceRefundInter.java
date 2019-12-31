package cn.trawe.pay.finance.account.service;

import com.alibaba.fastjson.JSONObject;

import cn.trawe.pay.finance.dto.BaseResp;

public interface FinanceRefundInter extends FinanceInterface {
	
	public BaseResp refund(JSONObject req);

}
