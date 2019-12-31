package cn.trawe.pay.finance.account.service.bus;

import org.springframework.stereotype.Service;

import cn.trawe.pay.finance.account.service.FinanceInterface;
import cn.trawe.pay.finance.enums.TransTypeEnum;

@Service
public class FinanceMqInterImpl implements FinanceMqInterface{
	
	


	@Override
	public FinanceInterface getInstance(TransTypeEnum busType) {
		// 继承spring接口，动态获取bean
		return (FinanceInterface) BeanUtil.getBean(busType.getType());
	}


	
	
	
	 
	

}
