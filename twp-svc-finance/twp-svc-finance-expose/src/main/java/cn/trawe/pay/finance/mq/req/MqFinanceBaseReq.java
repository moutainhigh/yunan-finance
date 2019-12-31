package cn.trawe.pay.finance.mq.req;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.enums.CustomerTypeEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import lombok.Data;

/**
 * 
 * @author jianjun.chai
 * RocketMq账户服务请求类
 * @param <T>
 */
@Data
public class MqFinanceBaseReq <T> {
	
	
	/**
	 *  CONSUME_TYPE("CA","消费"),
		RECHARGE_TYPE("RA","充值"),
    	WITHDRAWAL_TYPE("WA","提现"),
    	REFOUND_TYPE("RF","退款"),
	 */
	private TransTypeEnum busType;
	
	
	/**
	 * 业务内容 参考：/expose/finance/dto/reqAccountConsumeReq.java..
	 */
	private T data;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
		
	}
	
	

}
