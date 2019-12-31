package cn.trawe.pay.finance.account.service.bus;

import cn.trawe.pay.finance.account.service.FinanceInterface;
import cn.trawe.pay.finance.enums.CustomerTypeEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;

public interface FinanceMqInterface {
	
	
	FinanceInterface  getInstance(TransTypeEnum busType);

}
