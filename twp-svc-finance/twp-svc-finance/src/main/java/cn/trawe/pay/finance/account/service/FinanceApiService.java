
package cn.trawe.pay.finance.account.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.math.BigDecimal;

import cn.trawe.pay.finance.FinanceConstants;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;
import cn.trawe.pay.finance.dto.req.AccountRechargeReq;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
import cn.trawe.pay.finance.dto.req.AccountWithDrawReq;
import cn.trawe.pay.finance.dto.req.TradeUpdateReq;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.CustomerTypeEnum;
import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountLs;
import cn.trawe.pay.finance.useraccount.service.impl.UserRefundServiceImpl;
import cn.trawe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class FinanceApiService extends BaseServiceImpl   {
	

	@Autowired
	private UserRefundServiceImpl  UserRefundServiceImpl;
	
    

	//消费
    public BaseResp consume(AccountConsumeReq req) {
    	boolean isGetLock = false;
        String lockKey = ORDER_CONSUME_LOCKKEY + req.getOutOrderNo();
            try {
                try {
                    isGetLock = redisClient.tryLock(lockKey, TIMEOUT_LOCK, TRY_LOCK_COUNT);
                } catch (Throwable ex) {
                    LogUtil.error(log,  req.getOutOrderNo(), "获取锁出错异常：" + ex.getMessage());
                    return BaseResp.errorInstance(ex.getLocalizedMessage());
                   
                }
                if (!isGetLock) {
                    LogUtil.error(log,req.getOutOrderNo(),"重复请求" );
                    return BaseResp.errorInstance("重复请求");
                }
                //计算平台支出手续费金额
            	//平台手续费费率赋值
                
                if(AccountTypeEnum.WX_ACCOUNT.getAccount().equals(req.getAccountType())) {
                	req.setPlatServiceAmount(calculatePlateRateAmount(req.getAmount(),AccountTypeEnum.WX_ACCOUNT));
                	req.setPlatServiceRate(WX_PLATERATE.toString());
                }
                if(AccountTypeEnum.PLAT_ACCOUNT.getAccount().equals(req.getAccountType())) {
                	req.setPlatServiceAmount(0L);
                	req.setPlatServiceRate("0");
                }
                if(AccountTypeEnum.ALI_ACCOUNT.getAccount().equals(req.getAccountType())) {
                	req.setPlatServiceAmount(calculatePlateRateAmount(req.getAmount(),AccountTypeEnum.ALI_ACCOUNT));
                	req.setPlatServiceRate(ALI_PLATERATE.toString());
                }
                
            	return userService.consumeAccount(req.getUserId(), req.getMchId(), req.getAccountType(), 
            			req.getAmount(), req.getMerServiceAmount(), req.getMerServiceRate(), req.getPlatServiceAmount(), req.getPlatServiceRate(), req.getOutOrderNo(), req.getOutOrderType(), req.getTransDate(), "");
            	
                 
          }
          catch (Exception e) {
                 LogUtil.error(log, req.getOutOrderNo(), e.getMessage(), e.getCause());
                 throw e;
          } finally {
         if (isGetLock) {
             redisClient.unlock(lockKey);
         }
       }
    	
    	
    }
    
    //充值
    public BaseResp recharge(AccountRechargeReq req) {
    	
    	boolean isGetLock = false;
        String lockKey = ORDER_RECHARGE_LOCKKEY + req.getOutOrderNo();
            try {
                try {
                    isGetLock = redisClient.tryLock(lockKey, TIMEOUT_LOCK, TRY_LOCK_COUNT);
                } catch (Throwable ex) {
                    LogUtil.error(log,  req.getOutOrderNo(), "获取锁出错异常：" + ex.getMessage());
                    return BaseResp.errorInstance(ex.getLocalizedMessage());
                  
                }
                if (!isGetLock) {
                    LogUtil.error(log,  req.getOutOrderNo(), "重复请求" );
                    return BaseResp.errorInstance("重复请求");
                }
              
                if(AccountTypeEnum.WX_ACCOUNT.getAccount().equals(req.getAccountType())) {
                	req.setPlatServiceAmount(calculatePlateRateAmount(req.getAmount(),AccountTypeEnum.WX_ACCOUNT));
                	req.setPlatServiceRate(WX_PLATERATE.toString());
                }
                if(AccountTypeEnum.ALI_ACCOUNT.getAccount().equals(req.getAccountType())) {
                	req.setPlatServiceAmount(calculatePlateRateAmount(req.getAmount(),AccountTypeEnum.ALI_ACCOUNT));
                	req.setPlatServiceRate(ALI_PLATERATE.toString());
                }
            	//后续增加一个字段区分用户|商户|平台
            	if(CustomerTypeEnum.USER.equals(req.getCustomerType())) {
                	return userService.rechargeAccount(req.getUserId(), req.getAccountType(), req.getAmount(), req.getMerServiceAmount(), req.getMerServiceRate(), req.getPlatServiceAmount(), req.getPlatServiceRate(), req.getOutOrderNo(), req.getOutOrderType(), req.getTransDate(), "");
            	}
            	if(CustomerTypeEnum.MCH.equals(req.getCustomerType())) {
                	return merService.rechargeAccount(req.getUserId(), req.getAccountType(), req.getAmount(), req.getMerServiceAmount(), req.getMerServiceRate(), req.getPlatServiceAmount(), req.getPlatServiceRate(), req.getOutOrderNo(), req.getOutOrderType(), req.getTransDate(), "");
            	}
            	
        		throw TwFinanceException.BUSSINESS_ERROR;
          }
          catch (Exception e) {
                 LogUtil.error(log, req.getOutOrderNo(), e.getMessage(), e.getCause());
                 throw e;
          } finally {
         if (isGetLock) {
             redisClient.unlock(lockKey);
         }
       }
    	
    	
    }
    
    //提现
    public BaseResp withDraw(AccountWithDrawReq req) {

    	boolean isGetLock = false;
        String lockKey = ORDER_WITHDRAW_LOCKKEY + req.getOutOrderNo();
            try {
                try {
                    isGetLock = redisClient.tryLock(lockKey, TIMEOUT_LOCK, TRY_LOCK_COUNT);
                } catch (Throwable ex) {
                    LogUtil.error(log,  req.getOutOrderNo(), "获取锁出错异常：" + ex.getMessage());
                    return BaseResp.errorInstance(ex.getLocalizedMessage());
                  
                }
                if (!isGetLock) {
                    LogUtil.error(log,  req.getOutOrderNo(), "重复请求" );
                    return BaseResp.errorInstance("重复请求");
                }
                req.setPlatServiceAmount(0L);
                req.setPlatServiceRate("0");
            	req.setMerServiceAmount(0L);
            	req.setMerServiceRate("0");
            	req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
            	//后续增加一个字段区分用户|商户|平台
            	if(StringUtils.isNotBlank(req.getMchId())) {
            		req.setOutOrderType(FinanceConstants.ORDER_WITHDRAWAL_TYPE);
            		return merService.withdrawAccount(req.getMchId(), req.getAmount(), req.getOutOrderNo(), req.getOutOrderType(), req.getTransDate(), req.getPlatServiceAmount(), req.getPlatServiceRate(),req.getMerServiceAmount(),req.getMerServiceRate());
            	}
            	throw TwFinanceException.BUSSINESS_ERROR;
                
          }
          catch (Exception e) {
                 LogUtil.error(log, req.getOutOrderNo(), e.getMessage(), e.getCause());
                 throw e;
          } finally {
         if (isGetLock) {
             redisClient.unlock(lockKey);
         }
       }
    	
    	
    }
    
    
    //更新流水
    public BaseResp updateTradeNo(TradeUpdateReq req) {
    	
    	
    	throw TwFinanceException.BUSSINESS_ERROR;
    	
    }
    
    public BaseResp refund(AccountRefundReq req) {
    	return UserRefundServiceImpl.userRefund(req);
    }
    
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
