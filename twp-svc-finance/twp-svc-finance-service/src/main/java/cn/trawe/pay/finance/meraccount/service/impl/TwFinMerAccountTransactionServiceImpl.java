/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  TwFinMerAccountTransactionServiceImpl.java   
 * @Package cn.trawe.pay.finance.meraccount.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月15日 上午11:07:41   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.meraccount.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.math.BigDecimal;

import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.IncomeExpensesEnum;
import cn.trawe.pay.finance.enums.MerAccountStatusEnum;
import cn.trawe.pay.finance.enums.PlatAccountStatusEnum;
import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.enums.VirtualAccountStatusEnum;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.meraccount.entity.FinMchSubAccountBalance;
import cn.trawe.pay.finance.meraccount.service.TwFinMerAccountTransactionService;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountLs;
import cn.trawe.pay.finance.plataccount.entity.FinPlatSubAccountBalance;
import cn.trawe.util.L;


/**   
 * @ClassName:  TwFinMerAccountTransactionServiceImpl   
 * @Description:商户账户业务层实现类   
 * @author: jianjun.chai 
 * @date:   2019年11月15日 上午11:07:41   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Service
@lombok.extern.slf4j.Slf4j
public class TwFinMerAccountTransactionServiceImpl extends BaseServiceImpl implements TwFinMerAccountTransactionService {

	@Override
	public FinMchAccountBalance getDataByMerId(String merId,String accountType) {
		
		return  FinMchAccountBalanceDao.getDataByMerId(merId,accountType);
		
	}

//	@Override
//	public FinMchSubAccountBalance getSubDataByMchAccountNoAndAccountType(String mchAccountNo, String accountType) {
//		 List<FinMchSubAccountBalance> dataListByMchAccountNo = FinMchSubAccountBalanceDao.getDataListByMchAccountNo(mchAccountNo,accountType);
//		 if(dataListByMchAccountNo.isEmpty()) {
//			  throw TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("查询商户子账户返回空,子账户类型:{%s},商户账户号:{%s}", accountType,mchAccountNo);
//		  }
//		 return dataListByMchAccountNo.get(0);
//	}

//	@Override
//	public List<FinMchSubAccountBalance> getSubDataListByMchAccountNo(String mchAccountNo) {
//		  List<FinMchSubAccountBalance> dataListByMchAccountNo = FinMchSubAccountBalanceDao.getDataListByMchAccountNo(mchAccountNo,"");
//		  if(dataListByMchAccountNo.isEmpty()) {
//			  throw TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("查询商户子账户列表返回空,商户账户号:{%s}",mchAccountNo);
//		  }
//		  return dataListByMchAccountNo;
//	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp createMerAccount(String mchId, String mchName,
		   String settlementCycle,String accountType) {
		FinMchAccountBalance merBalance = FinMchAccountBalanceDao.getDataByMerId(mchId,accountType);
		if(merBalance == null) {
			merBalance = new FinMchAccountBalance();
			merBalance.setMchId(mchId);
			merBalance.setAccountType(accountType);
			merBalance.setMchName(mchName);
			merBalance.setOpenDate(new Date());
			merBalance.setAccountNo(createTradeNo("M",8));
			merBalance.setSettlementCycle(settlementCycle);
			merBalance.setStatus(MerAccountStatusEnum.OPEN.getStatus());
			merBalance.setVirtualAccountStatus(VirtualAccountStatusEnum.OPEN.getStatus());
			FinMchAccountBalanceDao.save(merBalance);
			//通过字段维护商户虚拟账户情况
//			FinMchSubAccountBalance  merSubBalance = new FinMchSubAccountBalance();
//			merSubBalance.setMchAccountNo(merBalance.getAccountNo());
//			merSubBalance.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
//			merSubBalance.setAccountNo(createTradeNo("MS",7));
//			merSubBalance.setAccountName(mchName);
//			merSubBalance.setOpenDate(new Date());
//			merSubBalance.setStatus(MerAccountStatusEnum.OPEN.getStatus());
//			FinMchSubAccountBalanceDao.save(merSubBalance);
			
		}
		else {
			merBalance.setVirtualAccountStatus(VirtualAccountStatusEnum.OPEN.getStatus());
			FinMchAccountBalanceDao.update(merBalance);
		}
		return BaseResp.successInstance();
		
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp createMerSubETCAccount(String mchId, String mchName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp<Object> withdrawAccount(String merId, Long amount, String outOrderNo,
			String outOrderType,Date transDate,Long platServiceAmount,String platServiceRate,Long merServiceAmount,String merServiceRate) {

		//查询商户提现流水
		FinMchAccountLs mchLsOld = FinMchAccountLsDao.getDataByOutOrderNo(outOrderNo,IncomeExpensesEnum.EXPENSES.getType());
		if(mchLsOld!=null) {
			
				log.info(L.b(outOrderNo).bizType("[商户提现]").msg("商户提现原交易已成功").s());
				throw  TwFinanceException.MER_ACCOUNT_RECHARGE_SUCCESSED;
			
		}
		//1)查询商户总账户、子账户（平台开立虚拟账户）记录、是否可用；
		FinMchAccountBalance merAccount = getDataByMerId(merId,AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		
		if(merAccount==null) {
				//商户不存在
			    log.error(L.b(outOrderNo).bizType("[商户提现]").msg("商户不存在-->商户ID :"+merId).s());
				throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("查询商户返回空.商户ID:{%s}", merId);
		}
		if(MerAccountStatusEnum.OPEN.getStatus()!=merAccount.getStatus()) {
				//商户不是正常状态
			    log.error(L.b(outOrderNo).bizType("[商户提现]").msg("商户状态不合法-->商户ID :"+merId).s());
				throw TwFinanceException.MER_ACCOUNT_RESULT_STATUS_ILLEGAL;
			
		}
		if(VirtualAccountStatusEnum.OPEN.getStatus()!=merAccount.getVirtualAccountStatus()) {
			//商户虚拟账户状态
			log.error(L.b(outOrderNo).bizType("[商户提现]").msg("商户虚拟账户未开通-->商户ID :"+merId).s());
			throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("商户虚拟账户未开通.商户ID:{%s}", merId);
		
	    }
		//查询平台总账户及子账户
//        FinPlatAccountBalance platAccount = FinPlatAccountBalanceDao.getData();
//        int compareTo = platAccount.getUseableBalance().compareTo(amount);
//        if(compareTo<0) {
//        	//可用余额不足
//        	log.info(L.b(outOrderNo).msg("平台总账户可用余额不足").s());
//        	throw TwFinanceException.PLAT_ACCOUNT_BALANCE_INSUFFICIENT;
//        }
        //TODO 商户提现默认从哪个子账户提款
        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(AccountTypeEnum.WX_ACCOUNT.getAccount());
        int compareToSub = platSubAccount.getUseableBalance().compareTo(amount);
        if(compareToSub<0) {
        	//可用余额不足
        	log.info(L.b(outOrderNo).bizType("[商户提现]").msg("平台账户->"+platSubAccount.getAccountName() +"可用余额不足").s());
        	throw TwFinanceException.PLAT_SUB_ACCOUNT_BALANCE_INSUFFICIENT.newInstance("平台账户可用余额不足,账户名称:{%s}", platSubAccount.getAccountName());
        }
//		2)商户总账户可用余额扣减提现金额、总余额扣减提现金额；
		FinMchAccountBalanceDao.subtractionTotalBalanceAndUseAbleBalance(merAccount.getId(), amount, outOrderNo);
		log.info(L.b(outOrderNo).bizType("[商户提现]").msg("商户账户变更成功--->商户账户号:"+merAccount.getAccountNo()).s());
//		3)商户账户交易流水表计入对应流水；
        FinMchAccountLs mchLs = new FinMchAccountLs();
        mchLs.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
        mchLs.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
        mchLs.setAccountNo(merAccount.getAccountNo());
        mchLs.setMerServiceAmount(merServiceAmount);
        mchLs.setMerServiceRate(merServiceRate);
        mchLs.setUseAccountNo(platSubAccount.getAccountNo());
        mchLs.setOutOrderNo(outOrderNo);
        mchLs.setOutOrderType(outOrderType);
        mchLs.setTransAmount(amount);
        mchLs.setTransDate(transDate);
        mchLs.setTransNo(createTradeNo());
        mchLs.setPlatServiceAmount(platServiceAmount);
        mchLs.setPlatServiceRate(platServiceRate);
        mchLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        mchLs.setTransType(TransTypeEnum.WITHDRAWAL_TYPE.getType());
        mchLs.setTotalBalance(new BigDecimal(merAccount.getTotalBalance()).subtract(new BigDecimal(amount)).longValue());
        FinMchAccountLsDao.save(mchLs);
        //TODO 如果商户提现存在手续就增加一条支出流水
        log.info(L.b(outOrderNo).bizType("[商户提现]").m("商户充值流水记账成功-->交易后余额:"+mchLs.getTotalBalance()).s());
       //4)平台账户可提现金额扣减提现金额、总余额扣减提现金额；      
        FinPlatSubAccountBalanceDao.subtractionTotalBalanceAndUseAbleBalance(platSubAccount.getId(),amount,outOrderNo);
        log.info(L.b(outOrderNo).bizType("[商户提现]").m("平台账户变更成功-->平台账户名称:"+platSubAccount.getAccountName()+"平台账户号:"+platSubAccount.getAccountNo()).s());
//		6)平台商户流水表计入对应流水；
        FinPlatAccountLs  platAccountLs = new FinPlatAccountLs();
        platAccountLs.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
        platAccountLs.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
        platAccountLs.setAccountNo(platSubAccount.getAccountNo());
        platAccountLs.setMchAccountNo(merAccount.getAccountNo());
        platAccountLs.setUseAccountNo(merAccount.getAccountNo());
        platAccountLs.setOutOrderNo(outOrderNo);
        platAccountLs.setOutOrderType(outOrderType);
        platAccountLs.setPlatServiceAmount(platServiceAmount);
        platAccountLs.setPlatServiceRate(platServiceRate);
        platAccountLs.setMerServiceAmount(merServiceAmount);
        platAccountLs.setMerServiceRate(merServiceRate);
        platAccountLs.setTransAmount(amount);
        platAccountLs.setTransType(TransTypeEnum.WITHDRAWAL_TYPE.getType());
        platAccountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        platAccountLs.setTransNo(createTradeNo());
        platAccountLs.setTransDate(transDate);
        platAccountLs.setTotalBalance(new BigDecimal(platSubAccount.getTotalBalance()).subtract(new BigDecimal(amount)).longValue());
        FinPlatAccountLsDao.save(platAccountLs);
        //TODO 如果提现存在手续费就新增一条平台支出流水
        log.info(L.b(outOrderNo).bizType("[商户提现]").m("平台交易流水记账成功--->交易后余额:"+platAccountLs.getTotalBalance()).s());
        return BaseResp.successInstance();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp rechargeAccount(String merId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl) {
		

		//
		//交易判重
		FinMchAccountLs mchLsOld = FinMchAccountLsDao.getDataByOutOrderNo(outOrderNo, IncomeExpensesEnum.INCOME.getType());
		if(mchLsOld!=null) {
			
			log.info(L.b(outOrderNo).bizType("[商户充值]").msg("商户充值原交易已成功").s());
			throw  TwFinanceException.MER_ACCOUNT_RECHARGE_SUCCESSED;
			
		}
//		1)查询商户总账户、子账户（平台开立虚拟账户）记录、是否可用；
		FinMchAccountBalance merAccount = getDataByMerId(merId,AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		
		if(merAccount==null) {
				//商户不存在
			    log.error(L.b(outOrderNo).bizType("[商户充值]").msg("商户不存在-->商户ID :"+merId).s());
				throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("查询商户返回空.商户ID:{%s}", merId);
		}
		if(MerAccountStatusEnum.OPEN.getStatus()!=merAccount.getStatus()) {
				//商户不是正常状态
			    log.error(L.b(outOrderNo).bizType("[商户充值]").msg("商户状态不合法-->商户ID :"+merId).s());
				throw TwFinanceException.MER_ACCOUNT_RESULT_STATUS_ILLEGAL;
			
		}
		if(VirtualAccountStatusEnum.OPEN.getStatus()!=merAccount.getVirtualAccountStatus()) {
			//商户虚拟账户状态
			log.error(L.b(outOrderNo).bizType("[商户充值]").msg("商户虚拟账户未开通-->商户ID :"+merId).s());
			throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("商户虚拟账户未开通.商户ID:{%s}", merId);
		
	    }
		//查询平台总账户及子账户
//        FinPlatAccountBalance platAccount = FinPlatAccountBalanceDao.getData();
//        if(PlatAccountStatusEnum.OPEN.getStatus()!=platAccount.getStatus()) {
//			//商户不是正常状态
//		    log.error(L.b(outOrderNo).msg("平台账户状态不合法-->商户ID :"+merId).s());
//			throw TwFinanceException.PLAT_ACCOUNT_RESULT_STATUS_ILLEGAL;
//		
//	    }
        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(accountType);
        if(PlatAccountStatusEnum.OPEN.getStatus()!=platSubAccount.getStatus()) {
			//平台账户不是正常状态
		    log.error(L.b(outOrderNo).bizType("[商户充值]").msg("平台账户状态不合法").s());
			throw TwFinanceException.PLAT_ACCOUNT_RESULT_STATUS_ILLEGAL.newInstance("平台账户--->"+platSubAccount.getAccountName()+"状态不合法");
		
	    }
        
//		2)商户总账户总余额增加充值金额，可提现金额增加金额（交易金额-平台支出手续费金额）；
        FinMchAccountLs mchLs = new FinMchAccountLs();
        FinPlatAccountLs  platAccountLs = new FinPlatAccountLs();
        long addAmount = 0L;
        if(isMerRecharge) {
        	addAmount = amount - platServiceAmount;
        	platAccountLs.setMerServiceAmount(platServiceAmount);
            platAccountLs.setMerServiceRate(platServiceRate);
            mchLs.setMerServiceAmount(platServiceAmount);
            mchLs.setMerServiceRate(platServiceRate);
            
            FinMchAccountBalanceDao.addTotalBalanceAndUseAbleBalanceAndMerServiceAmount(merAccount.getId(),addAmount,outOrderNo,platServiceAmount);
            log.info(L.b(outOrderNo).bizType("[商户充值]").bizType("收取商户充值手续费").msg("商户账户变更成功--->商户账户号:"+merAccount.getAccountNo()).s());
        }
        else {
        	addAmount = amount;
        	mchLs.setMerServiceAmount(merServiceAmount);
            mchLs.setMerServiceRate(merServiceRate);
        	platAccountLs.setMerServiceAmount(merServiceAmount);
            platAccountLs.setMerServiceRate(merServiceRate);
            FinMchAccountBalanceDao.addTotalBalanceAndUseAbleBalance(merAccount.getId(),addAmount,outOrderNo);
            log.info(L.b(outOrderNo).bizType("[商户充值]").bizType("不收取商户充值手续费").msg("商户账户变更成功--->商户账户号:"+merAccount.getAccountNo()).s());
        }
        
		
//		3)商户账户交易流水表计入对应流水；（交易类型充值）
		
        mchLs.setAccountNo(merAccount.getAccountNo());
        mchLs.setAccountType(accountType);
        mchLs.setUseAccountNo(platSubAccount.getAccountNo());
        mchLs.setOutOrderNo(outOrderNo);
        mchLs.setOutOrderType(outOrderType);
        mchLs.setTransAmount(amount);
        mchLs.setTransDate(transDate);
        mchLs.setTransNo(createTradeNo());
        mchLs.setPlatServiceAmount(platServiceAmount);
        mchLs.setPlatServiceRate(platServiceRate);
        mchLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        mchLs.setTransType(TransTypeEnum.RECHARGE_TYPE.getType());
        mchLs.setTotalBalance(new BigDecimal(merAccount.getTotalBalance()).add(new BigDecimal(addAmount)).longValue());
        FinMchAccountLsDao.save(mchLs);
        log.info(L.b(outOrderNo).bizType("[商户充值]").m("商户充值流水记账成功-->交易后余额:"+mchLs.getTotalBalance()).s());
	     Long  addPlatSubAmount = amount - platServiceAmount;
	    // 4)平台账户总余额增加金额（交易金额-平台支出手续费金额），可提现余额增加金额（交易金额-平台支出手续费金额）、平台支出手续费增加平台手续费金额（平台和银行交易的手续费金额）；       
	     FinPlatSubAccountBalanceDao.AddTotalBalanceAndUseAbleBalanceAndPlatServiceAmount(platSubAccount.getId(), addPlatSubAmount,outOrderNo,platServiceAmount);
//		5)平台商户流水表计入对应流水；（交易类型充值）
	     log.info(L.b(outOrderNo).bizType("[商户充值]").m("平台账户变更成功-->平台账户名称:"+platSubAccount.getAccountName()+"平台账户号:"+platSubAccount.getAccountNo()).s());
         platAccountLs.setAccountNo(platSubAccount.getAccountNo());
         platAccountLs.setMchAccountNo(platSubAccount.getAccountNo());
         platAccountLs.setAccountType(accountType);
         platAccountLs.setUseAccountNo(merAccount.getAccountNo());
         platAccountLs.setOutOrderNo(outOrderNo);
         platAccountLs.setOutOrderType(outOrderType);
         
         platAccountLs.setPlatServiceAmount(platServiceAmount);
         platAccountLs.setPlatServiceRate(platServiceRate);
         platAccountLs.setTransAmount(amount);
         platAccountLs.setTransType(TransTypeEnum.RECHARGE_TYPE.getType());
         platAccountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
         platAccountLs.setTransNo(createTradeNo());
         platAccountLs.setTransDate(transDate);
         platAccountLs.setTotalBalance(new BigDecimal(platSubAccount.getTotalBalance()).add(new BigDecimal(addPlatSubAmount)).longValue());
         FinPlatAccountLsDao.save(platAccountLs);
         log.info(L.b(outOrderNo).bizType("[商户充值]").m("平台交易流水记账成功--->交易后余额:"+platAccountLs.getTotalBalance()).s());
         return BaseResp.successInstance();
		
	}

	@Override
	public BaseResp refundAccount(String userId, String merId, String accountType, Long amount, Long merServiceAmount,
			String merServiceRate, Long platServiceAmount, String platServiceRate, String outOrderNo,
			String outOrderType, Date transDate, String notifyUrl) {
		// TODO Auto-generated method stub
		return null;
	}

}
