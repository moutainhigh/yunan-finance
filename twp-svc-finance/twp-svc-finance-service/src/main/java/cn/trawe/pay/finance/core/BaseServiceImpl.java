/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  BaseServiceImpl.java   
 * @Package cn.trawe.pay.finance.core   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月12日 下午6:06:49   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.core;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.math.BigDecimal;

import cn.trawe.pay.common.client.RedisClient;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountBalanceDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountLsDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchSettleBillDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchSubAccountBalanceDao;
import cn.trawe.pay.finance.meraccount.service.impl.TwFinMerAccountTransactionServiceImpl;
import cn.trawe.pay.finance.plataccount.dao.FinPlatAccountBalanceDao;
import cn.trawe.pay.finance.plataccount.dao.FinPlatAccountLsDao;
import cn.trawe.pay.finance.plataccount.dao.FinPlatSettleBillDao;
import cn.trawe.pay.finance.plataccount.dao.FinPlatSubAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountLsDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserSettleBillDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserSubAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.service.impl.TwFinUserAccountTransactionServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  BaseServiceImpl   
 * @Description:业务层基类
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午6:06:49   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Service
@Slf4j
public class BaseServiceImpl {
	
	
	@Autowired
	protected RedisClient redisClient;
	
	@Autowired
	protected FinUserAccountBalanceDao  FinUserAccountBalanceDao;
	
	@Autowired
	protected FinUserAccountLsDao  FinUserAccountLsDao;
	
	@Autowired
	protected FinUserSettleBillDao  FinUserSettleBillDao;
	
	@Autowired
	protected FinUserSubAccountBalanceDao  FinUserSubAccountBalanceDao;
	
	@Autowired
	protected FinMchAccountBalanceDao  FinMchAccountBalanceDao;
	
	@Autowired
	protected FinMchAccountLsDao FinMchAccountLsDao;
	
	@Autowired
	protected FinMchSettleBillDao  FinMchSettleBillDao;
	
	@Autowired
	protected FinMchSubAccountBalanceDao  FinMchSubAccountBalanceDao;
	
	@Autowired
	protected FinPlatAccountBalanceDao  FinPlatAccountBalanceDao;
	
	@Autowired
	protected FinPlatAccountLsDao  FinPlatAccountLsDao;
	
	@Autowired
	protected FinPlatSettleBillDao  FinPlatSettleBillDao;
	
	@Autowired
	protected FinPlatSubAccountBalanceDao  FinPlatSubAccountBalanceDao;
	
	
	@Autowired
	protected TwFinUserAccountTransactionServiceImpl userService;
	
	@Autowired
	protected TwFinMerAccountTransactionServiceImpl  merService;
	
	//是否收商户充值手续费  true 收  false 不收
	//@Value("${is_mer_recharge}")
	protected boolean  isMerRecharge  =true;
	
	@Autowired
	protected CreateTradeNoService CreateTradeNoService;
	
	protected String  createTradeNo(Date tradeTime, String type, int length) {
		 return CreateTradeNoService.createTradeNo(tradeTime, type, length);
	}
	
	protected String  createTradeNo() {
		 return CreateTradeNoService.createTradeNo(new Date(), "", 10);
	}
	
	protected String  createTradeNo(String type, int length) {
		 return CreateTradeNoService.createTradeNo(new Date(), type, length);
	}
	
	protected String  createTradeNo(int length) {
		 return CreateTradeNoService.createTradeNo(new Date(), "", length);
	}
	
	protected   static final BigDecimal WX_PLATERATE =new BigDecimal("0.006");
	
	protected   static final BigDecimal ALI_PLATERATE =new BigDecimal("0.006");
	
	protected Long calculatePlateRateAmount(Long amount,AccountTypeEnum type) {
		
		
		if(AccountTypeEnum.WX_ACCOUNT.equals(type)) {
			BigDecimal amountBig = new BigDecimal(amount);
	    	BigDecimal multiply = WX_PLATERATE.multiply(amountBig);
	    	log.info("平台支出手续费金额:"+multiply.doubleValue());
	        //四舍五入
	        Long result = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	        log.info("平台支出手续费金额四舍五入后:"+result);
	        return result;
		}
		if(AccountTypeEnum.ALI_ACCOUNT.equals(type)) {
			BigDecimal amountBig = new BigDecimal(amount);
	    	BigDecimal multiply = ALI_PLATERATE.multiply(amountBig);
	    	log.info("平台支出手续费金额:"+multiply.doubleValue());
	        //四舍五入
	        Long result = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	        log.info("平台支出手续费金额四舍五入后:"+result);
	        return result;
		}
		throw TwFinanceException.BUSSINESS_ERROR.newInstance("未查询到正确的平台支付配置费率,请检查支付类型,当前请求支付类型为:{%s}", type.getAccount());
		
	}
	
	protected  String ORDER_CONSUME_LOCKKEY = "finance:order:consume:lockkey:";
	
	protected  String ORDER_RECHARGE_LOCKKEY = "finance:order:recharge:lockkey:";
	
	protected  String ORDER_REFUND_LOCKKEY = "finance:order:refund:lockkey:";
	
	protected  String ORDER_WITHDRAW_LOCKKEY = "finance:order:withdraw:lockkey:";
	
    protected  int TIMEOUT_LOCK = 60;
    /**
     * 获取锁的次数
     */
    protected  int TRY_LOCK_COUNT = 3;
	

	
    
    public static void main(String[] args) {
    	
    	BigDecimal aBigDecimal = new BigDecimal(0.006);
		BigDecimal bBigDecimal = new BigDecimal(250L);
    	BigDecimal multiply = aBigDecimal.multiply(bBigDecimal);
    	System.out.println(multiply.doubleValue());
//    	Long num = 1L;
//        BigDecimal b = new BigDecimal(num);
        //保留2位小数
        Long result = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        System.out.println(result);  //111231.56

	}
    

	
	
	
	
	

}
