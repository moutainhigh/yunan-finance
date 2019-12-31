/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  TwFinUserAccountTransactionServiceImpl.java   
 * @Package cn.trawe.pay.finance.useraccount.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月12日 下午5:59:50   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.useraccount.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.math.BigDecimal;

import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.enums.AccountStatusEnum;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.IncomeExpensesEnum;
import cn.trawe.pay.finance.enums.MerAccountStatusEnum;
import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.enums.VirtualAccountStatusEnum;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.meraccount.service.TwFinMerAccountTransactionService;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountLs;
import cn.trawe.pay.finance.plataccount.entity.FinPlatSubAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;
import cn.trawe.pay.finance.useraccount.entity.FinUserSubAccountBalance;
import cn.trawe.pay.finance.useraccount.service.TwFinUserAccountTransactionService;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  TwFinUserAccountTransactionServiceImpl   
 * @Description:用户账户实现类   
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午5:59:50   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Service
@Slf4j
public class TwFinUserAccountTransactionServiceImpl extends BaseServiceImpl implements TwFinUserAccountTransactionService  {
	
	@Autowired
	TwFinMerAccountTransactionService  TwFinMerAccountTransactionService;

    @Override
	public FinUserAccountBalance getByUserId(String userId,String accountType, boolean isForUpdate) {
		 
		 FinUserAccountBalance account = FinUserAccountBalanceDao.getDataByUserId(userId,accountType,isForUpdate);
		 return account;
	}
	
	@Override
	public BaseResp getDataByUserId(String userId, String accountType,boolean isForUpdate) {
		 
		 FinUserAccountBalance account = FinUserAccountBalanceDao.getDataByUserId(userId,accountType,isForUpdate);
		 
		 if(account!=null) {
			 if(VirtualAccountStatusEnum.OPEN.getStatus()==account.getVirtualAccountStatus()) {
					BaseResp successInstance = BaseResp.successInstance();
					successInstance.setData(account);
					return successInstance;
			 }
		 }
		return BaseResp.successInstance();
		 
		 
		
	}

//	@Override
//	public List<FinUserSubAccountBalance> getDataListByAccountNo(String userAccountNo) {
//		
//		List<FinUserSubAccountBalance> accountSubList = FinUserSubAccountBalanceDao.getDataListByUserAccountNo(userAccountNo);
//		return accountSubList;
//	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp createUserAccount(String userId,
			String userName,String accountType) {
		
		FinUserAccountBalance account = FinUserAccountBalanceDao.getDataByUserId(userId,accountType,false);
		if(account==null) {
			account = new FinUserAccountBalance();
			account.setUserId(userId);
			account.setAccountType(accountType);
			account.setUserName(userName);
			account.setAccountNo(createTradeNo("U", 8));
			account.setOpenDate(new Date());
			account.setStatus(AccountStatusEnum.OPEN.getStatus());
			account.setVirtualAccountStatus(VirtualAccountStatusEnum.OPEN.getStatus());
			FinUserAccountBalanceDao.save(account);
		}
		else {
			account.setVirtualAccountStatus(VirtualAccountStatusEnum.OPEN.getStatus());
			account.setUserName(userName);
			FinUserAccountBalanceDao.update(account);
		}
		
		return BaseResp.successInstance();
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp consumeAccount(String userId,String merId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl) {
		
         //redis 加锁防止重复调用  outOrderNo --> redis-key 
		//交易判重
		FinMchAccountLs mchLsOld = FinMchAccountLsDao.getDataByOutOrderNo(outOrderNo,IncomeExpensesEnum.INCOME.getType());
		if(mchLsOld!=null) {
			log.info(L.b(outOrderNo).bizType("[用户消费]").msg("用户消费原交易已成功").s());
			throw  TwFinanceException.MER_ACCOUNT_RECHARGE_SUCCESSED;
			
		}
//	    3)查询商户子账户记录、查询商户总账户记录；
		FinMchAccountBalance merAccount = TwFinMerAccountTransactionService.getDataByMerId(merId,AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		if(merAccount==null) {
			//商户不存在
			throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("数据库操作, query 返回空.商户ID:{%s}", merId);
		}
		if(MerAccountStatusEnum.OPEN.getStatus()!=merAccount.getStatus()) {
			//商户不是正常状态
			throw TwFinanceException.MER_ACCOUNT_RESULT_STATUS_ILLEGAL;
		}
		if(VirtualAccountStatusEnum.OPEN.getStatus()!=merAccount.getVirtualAccountStatus()) {
			//商户虚拟账户状态
			log.error(L.b(outOrderNo).bizType("[用户消费]").msg("商户虚拟账户未开通-->商户ID :"+merId).s());
			throw  TwFinanceException.MER_ACCOUNT_RESULT_0.newInstance("商户虚拟账户未开通.商户ID:{%s}", merId);
		
	    }
	
		//平台虚拟账户消费
		if(AccountTypeEnum.PLAT_ACCOUNT.getAccount().equals(accountType)) {
			
			log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("用户余额消费开始...用户ID-->"+userId+"商户ID-->"+merId).s());
//			1)查询用户子账户、总账户（平台开立虚拟账户）记录；
			FinUserAccountBalance account = getByUserId(userId,accountType,false);
			if(account==null) {
				//账户不存在
				throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("数据库操作, query 返回空.用户ID:{%s}", userId);
			}
			if(AccountStatusEnum.OPEN.getStatus()!=account.getStatus()) {
				//用户不是正常状态
				throw TwFinanceException.ACCOUNT_RESULT_STATUS_ILLEGAL;
			}
			if(VirtualAccountStatusEnum.OPEN.getStatus()!=account.getVirtualAccountStatus()) {
				//商户虚拟账户状态
				log.error(L.b(outOrderNo).bizType("[用户余额消费]").msg("用户虚拟账户未开通-->用户ID :"+userId).s());
				throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("用户虚拟账户未开通.用户ID:{%s}", userId);
			
		    }
			
			//2)可用余额是否大于等于交易金额
			int compareTo = account.getUseableBalance().compareTo(amount);
	        if(compareTo<0) {
	        	//可用余额不足
	        	log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("用户可用余额不足,用户可用余额->"+account.getUseableBalance()+"用户账户号->"+account.getAccountNo()).s());
	        	throw TwFinanceException.ACCOUNT_BALANCE_INSUFFICIENT;
	        }
	        //FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(accountType);
	        //4)用户总账户可用余额扣减对应交易金额，总余额扣减对应交易金额；
	        FinUserAccountBalanceDao.subtractionTotalBalanceAndUseAbleBalance(account.getId(),amount,outOrderNo);
	        log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("用户账户变更成功--->用户账户号:"+account.getAccountNo()).s());
            //5)用户账务流水表记录对应交易流水；
	        FinUserAccountLs   accountLs = new FinUserAccountLs();
	        accountLs.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
	        accountLs.setAccountNo(account.getAccountNo());
	        accountLs.setAccountType(accountType);
	        accountLs.setMchAccountNo(merAccount.getAccountNo());
	        accountLs.setOutOrderNo(outOrderNo);
	        accountLs.setOutOrderType(outOrderType);
	        accountLs.setMerServiceAmount(merServiceAmount);
	        accountLs.setMerServiceRate(merServiceRate);
	        accountLs.setPlatServiceAmount(platServiceAmount);
	        accountLs.setPlatServiceRate(platServiceRate);
	        accountLs.setTransAmount(amount);
	        accountLs.setTransDate(transDate);
	        accountLs.setTransNo(createTradeNo());
	        accountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        accountLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
	        accountLs.setTotalBalance(new BigDecimal(account.getTotalBalance()).subtract(new BigDecimal(amount)).longValue());
	        FinUserAccountLsDao.save(accountLs);
	        log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("用户消费流水记账成功-->交易后余额:"+accountLs.getTotalBalance()).s());
	        //6)商户对应的总账户收入金额增加对应（交易金额-商户支出手续费），总余额增加对应金额（交易金额-商户支出手续费）、商户支出增加对应商户支出手续费；
	        //计算手续费后金额 
	        long addMerAmount = amount - merServiceAmount;
	        FinMchAccountBalanceDao.addTotalBalanceAndFreezeBalance(merAccount.getId(),addMerAmount,merServiceAmount,outOrderNo);
	        log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("商户账户变更成功--->商户账户名称:"+merAccount.getMchName()+"商户账户号:"+merAccount.getAccountNo()).s());
	        //7)商户账户交易流水表记录对应流水；
	        FinMchAccountLs mchLs = new FinMchAccountLs();
	        mchLs.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
	        mchLs.setAccountNo(merAccount.getAccountNo());
	        mchLs.setAccountType(accountType);
	        mchLs.setUseAccountNo(account.getAccountNo());
	        mchLs.setOutOrderNo(outOrderNo);
	        mchLs.setOutOrderType(outOrderType);
	        mchLs.setTransAmount(amount);
	        mchLs.setMerServiceAmount(merServiceAmount);
	        mchLs.setMerServiceRate(merServiceRate);
	        mchLs.setPlatServiceAmount(platServiceAmount);
	        mchLs.setPlatServiceRate(platServiceRate);
	        mchLs.setTransDate(transDate);
	        String mchTradeNo =createTradeNo();
	        mchLs.setTransNo(mchTradeNo);
	        mchLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        mchLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
	        mchLs.setTotalBalance(new BigDecimal(merAccount.getTotalBalance()).add(new BigDecimal(addMerAmount)).longValue());
	        FinMchAccountLsDao.save(mchLs);
	        FinMchAccountLs mchLsExpenses = new FinMchAccountLs();
	        mchLsExpenses.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
	        mchLsExpenses.setAccountNo(merAccount.getAccountNo());
	        mchLsExpenses.setAccountType(accountType);
	        mchLsExpenses.setUseAccountNo(account.getAccountNo());
	        mchLsExpenses.setOutOrderNo(outOrderNo);
	        mchLsExpenses.setOutOrderType(outOrderType);
	        mchLsExpenses.setTransAmount(amount);
	        mchLsExpenses.setMerServiceAmount(merServiceAmount);
	        mchLsExpenses.setMerServiceRate(merServiceRate);
	        mchLsExpenses.setTransDate(transDate);
	        mchLsExpenses.setTransNo(mchTradeNo);
	        mchLsExpenses.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        mchLsExpenses.setTransType(TransTypeEnum.SERVICE_CHARGE.getType());
	        mchLsExpenses.setMemo(TransTypeEnum.SERVICE_CHARGE.getDesc()+"|"+outOrderNo);
	        FinMchAccountLsDao.save(mchLsExpenses);
	        
	        log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("商户交易流水记账成功 --->交易后余额:"+mchLs.getTotalBalance()).s());
	        //8)平台收入手续费增加手续费金额、平台支出手续费不变更。？
//	        FinPlatSubAccountBalanceDao.addPlatServiceAmount(platSubAccount.getId(), outOrderNo,merServiceAmount);
//	        //9)平台商户交易流水表记录对应流水；
//	        FinPlatAccountLs  platAccountLs = new FinPlatAccountLs();
//	        platAccountLs.setAccountNo(platSubAccount.getAccountNo());
//	        platAccountLs.setAccountType(accountType);
//	        platAccountLs.setMchAccountNo(merAccount.getAccountNo());
//	        platAccountLs.setUseAccountNo(account.getAccountNo());
//	        platAccountLs.setOutOrderNo(outOrderNo);
//	        platAccountLs.setOutOrderType(outOrderType);
//	        platAccountLs.setMerServiceAmount(merServiceAmount);
//	        platAccountLs.setMerServiceRate(merServiceRate);
//	        platAccountLs.setPlatServiceAmount(platServiceAmount);
//	        platAccountLs.setPlatServiceRate(platServiceRate);
//	        platAccountLs.setTransAmount(amount);
//	        platAccountLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
//	        platAccountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
//	        platAccountLs.setTransNo(createTradeNo());
//	        platAccountLs.setTransDate(transDate);
//	        FinPlatAccountLsDao.save(platAccountLs);
	        //9)异步回调通知聚合支付消费结果
	        //TODO
	        log.info(L.b(outOrderNo).bizType("[用户余额消费]").m("用户余额消费成功,用户ID-->"+userId+"商户ID-->"+merId).s());
		}
		else {
//			1)查询用户子账户、总账户（平台开立虚拟账户）记录；
			log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("用户第三方支付消费开始...用户ID-->"+userId+"商户ID-->"+merId).s());
			FinUserAccountBalance account = getByUserId(userId,AccountTypeEnum.PLAT_ACCOUNT.getAccount(),false);
			if(account==null) {
				//初始化总账户
				account = new FinUserAccountBalance();
				account.setOpenDate(new Date());
				account.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
				account.setAccountNo(createTradeNo("U", 8));
				account.setUserId(userId);
				account.setStatus(AccountStatusEnum.OPEN.getStatus());
				FinUserAccountBalanceDao.save(account);
			}
//			3）用户账户交易流水表记录对应交易流水；
		    FinUserAccountLs   accountLs = new FinUserAccountLs();
		    accountLs.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
	        accountLs.setAccountNo(account.getAccountNo());
	        accountLs.setAccountType(accountType);
	        accountLs.setMchAccountNo(merAccount.getAccountNo());
	        accountLs.setOutOrderNo(outOrderNo);
	        accountLs.setOutOrderType(outOrderType);
	        accountLs.setMerServiceAmount(merServiceAmount);
	        accountLs.setMerServiceRate(merServiceRate);
	        accountLs.setPlatServiceAmount(platServiceAmount);
	        accountLs.setPlatServiceRate(platServiceRate);
	        accountLs.setTransAmount(amount);
	        accountLs.setTransDate(transDate);
	        accountLs.setTransNo(createTradeNo());
	        accountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        accountLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
	        accountLs.setTotalBalance(BigDecimal.ZERO.longValue());
	        FinUserAccountLsDao.save(accountLs);
	        log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("用户消费流水记账成功--->用户账户号:"+account.getAccountNo()).s());
//			4）查询平台总账户、对应支付方式平台子账户记录；
	        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(accountType);
	        Long  addPlatSubAmount  =amount -platServiceAmount;
//			6）平台账户冻结余额增加对应（交易金额-平台支出手续费）、平台支出手续费金额增加平台和银行交易的手续费、总余额增加对应金额（交易金额-平台支出手续费）；
	        FinPlatSubAccountBalanceDao.addTotalBalanceAndFreezeBalanceAndPlatServiceAmount(platSubAccount.getId(),addPlatSubAmount,outOrderNo,platServiceAmount);
            log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("平台账户变更成功-->平台账户名称:"+platSubAccount.getAccountName()+"平台账户号:"+platSubAccount.getAccountNo()).s());
	        //7）平台商户交易流水表记录对应流水；
	        FinPlatAccountLs  platAccountLs = new FinPlatAccountLs();
	        platAccountLs.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
	        platAccountLs.setAccountType(accountType);
	        platAccountLs.setAccountNo(platSubAccount.getAccountNo());
	        platAccountLs.setMchAccountNo(merAccount.getAccountNo());
	        platAccountLs.setUseAccountNo(account.getAccountNo());
	        platAccountLs.setOutOrderNo(outOrderNo);
	        platAccountLs.setOutOrderType(outOrderType);
	        platAccountLs.setMerServiceAmount(merServiceAmount);
	        platAccountLs.setMerServiceRate(merServiceRate);
	        platAccountLs.setPlatServiceAmount(platServiceAmount);
	        platAccountLs.setPlatServiceRate(platServiceRate);
	        platAccountLs.setTransAmount(amount);
	        platAccountLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
	        platAccountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        String plateTradeNo =createTradeNo();
	        platAccountLs.setTransNo(plateTradeNo);
	        platAccountLs.setTransDate(transDate);
	        platAccountLs.setTotalBalance(new BigDecimal(platSubAccount.getTotalBalance()).add(new BigDecimal(addPlatSubAmount)).longValue());
	        FinPlatAccountLsDao.save(platAccountLs);
	        FinPlatAccountLs  platAccountLsExpenses = new FinPlatAccountLs();
	        platAccountLsExpenses.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
	        platAccountLsExpenses.setAccountType(accountType);
	        platAccountLsExpenses.setAccountNo(platSubAccount.getAccountNo());
	        platAccountLsExpenses.setMchAccountNo(merAccount.getAccountNo());
	        platAccountLsExpenses.setUseAccountNo(account.getAccountNo());
	        platAccountLsExpenses.setOutOrderNo(outOrderNo);
	        platAccountLsExpenses.setOutOrderType(outOrderType);
	        platAccountLsExpenses.setPlatServiceAmount(platServiceAmount);
	        platAccountLsExpenses.setPlatServiceRate(platServiceRate);
	        platAccountLsExpenses.setTransAmount(amount);
	        platAccountLsExpenses.setTransType(TransTypeEnum.SERVICE_CHARGE.getType());
	        platAccountLsExpenses.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        platAccountLsExpenses.setTransNo(plateTradeNo);
	        platAccountLsExpenses.setTransDate(transDate);
	        platAccountLsExpenses.setMemo(TransTypeEnum.SERVICE_CHARGE.getDesc()+"|"+outOrderNo);
	        FinPlatAccountLsDao.save(platAccountLsExpenses);
	        log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("平台交易流水记账成功-->交易后总余额:"+platAccountLs.getTotalBalance()).s());
	       // 商户总账户收入金额增加对应（交易金额-商户支出手续费），总余额增加对应金额（交易金额-商户支出手续费）、商户支出手续费增加平台对商户收取的手续费金额
	        long addAmount = amount - merServiceAmount ;
	        FinMchAccountBalanceDao.addTotalBalanceAndFreezeBalance(merAccount.getId(),addAmount,merServiceAmount,outOrderNo); 
	        log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("商户账户变更成功--->商户账户名称:"+merAccount.getMchName()+"商户账户号:"+merAccount.getAccountNo()).s());
//			9）商户账户交易流水表记录对应流水；
	        FinMchAccountLs mchLs = new FinMchAccountLs();
	        mchLs.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
	        mchLs.setAccountNo(merAccount.getAccountNo());
	        mchLs.setMerServiceAmount(merServiceAmount);
	        mchLs.setMerServiceRate(merServiceRate);
	        mchLs.setAccountType(accountType);
	        mchLs.setUseAccountNo(account.getAccountNo());
	        mchLs.setOutOrderNo(outOrderNo);
	        mchLs.setOutOrderType(outOrderType);
	        mchLs.setTransAmount(amount);
	        mchLs.setTransDate(transDate);
	        String mchTradeNo = createTradeNo();
	        mchLs.setTransNo(mchTradeNo);
	        mchLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        mchLs.setTransType(TransTypeEnum.CONSUME_TYPE.getType());
	        mchLs.setPlatServiceAmount(platServiceAmount);
	        mchLs.setPlatServiceRate(platServiceRate);
	        mchLs.setTotalBalance(new BigDecimal(merAccount.getTotalBalance()).add(new BigDecimal(addAmount)).longValue());
	        FinMchAccountLsDao.save(mchLs);
	        FinMchAccountLs mchLsExpenses = new FinMchAccountLs();
	        mchLsExpenses.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
	        mchLsExpenses.setAccountNo(merAccount.getAccountNo());
	        mchLsExpenses.setMerServiceAmount(merServiceAmount);
	        mchLsExpenses.setMerServiceRate(merServiceRate);
	        mchLsExpenses.setAccountType(accountType);
	        mchLsExpenses.setUseAccountNo(account.getAccountNo());
	        mchLsExpenses.setOutOrderNo(outOrderNo);
	        mchLsExpenses.setOutOrderType(outOrderType);
	        mchLsExpenses.setTransAmount(amount);
	        mchLsExpenses.setTransDate(transDate);
	        mchLsExpenses.setTransNo(mchTradeNo);
	        mchLsExpenses.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	        mchLsExpenses.setTransType(TransTypeEnum.SERVICE_CHARGE.getType());
	        FinMchAccountLsDao.save(mchLsExpenses);
	        log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("商户流水记账成功-->交易后总余额:"+mchLs.getTotalBalance()).s());
			log.info(L.b(outOrderNo).bizType("[用户第三方支付消费]").m("用户第三方支付消费结束...用户ID-->"+userId+"商户ID-->"+merId).s());
		}
	    return BaseResp.successInstance();
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@Deprecated
	public FinUserAccountBalance withdrawAccount(String userId, Long amount, String outOrderNo,
			String outOrderType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BaseResp rechargeAccount(String userId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl) {
		
		//交易判重
		FinUserAccountLs accountLsOld = FinUserAccountLsDao.getDataByOutOrderNo(outOrderNo, outOrderType, amount);
		if(accountLsOld!=null) {
			
				log.info(L.b(outOrderNo).bizType("[用户充值]").msg("用户充值原交易已成功").s());
				throw  TwFinanceException.MER_ACCOUNT_RECHARGE_SUCCESSED;
			
		}
//		1)查询用户总账户、子账户（平台开立虚拟账户）记录、是否可用；
		FinUserAccountBalance account = getByUserId(userId,AccountTypeEnum.PLAT_ACCOUNT.getAccount(),false);
		if(account==null) {
			//账户不存在
			log.info(L.b(outOrderNo).bizType("[用户充值]").msg("用户总账户未查询到.用户ID:"+userId).s());
			throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("用户总账户未查询到.用户ID:{%s}", userId);
		}
		if(AccountStatusEnum.OPEN.getStatus()!=account.getStatus()) {
			//用户不是正常状态
			log.info(L.b(outOrderNo).bizType("[用户充值]").msg("用户账户状态不正常.用户ID:"+userId).s());
			throw TwFinanceException.ACCOUNT_RESULT_STATUS_ILLEGAL;
		}
		
		if(VirtualAccountStatusEnum.OPEN.getStatus()!=account.getVirtualAccountStatus()) {
			//商户虚拟账户状态
			log.error(L.b(outOrderNo).bizType("[用户充值]").msg("用户虚拟账户未开通-->用户ID :"+userId).s());
			throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("用户虚拟账户未开通.用户ID:{%s}", userId);
		
	    }
//		2)用户总账户总余额增加充值金额，可用余额增加充值金额；
		FinUserAccountBalanceDao.addTotalBalanceAndUseAbleBalance(account.getId(), amount, outOrderNo);
		log.info(L.b(outOrderNo).bizType("[用户充值]").msg("用户账户变更成功--->用户账户号:"+account.getAccountNo()).s());
//		3)用户账户交易流水表计入对应流水；（交易类型充值）
		//FinPlatAccountBalance platAccount = FinPlatAccountBalanceDao.getData();
		//飞哥会指定支付方式
        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(accountType);
		 FinUserAccountLs   accountLs = new FinUserAccountLs();
		 accountLs.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
	     accountLs.setAccountNo(account.getAccountNo());
	     accountLs.setAccountType(accountType);
	     accountLs.setMchAccountNo(platSubAccount.getAccountNo());
	     accountLs.setOutOrderNo(outOrderNo);
	     accountLs.setOutOrderType(outOrderType);
	     accountLs.setMerServiceAmount(merServiceAmount);
	     accountLs.setMerServiceRate(merServiceRate);
	     accountLs.setPlatServiceAmount(platServiceAmount);
	     accountLs.setPlatServiceRate(platServiceRate);
	     accountLs.setTransAmount(amount);
	     accountLs.setTransDate(transDate);
	     accountLs.setTransNo(createTradeNo());
	     accountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
	     accountLs.setTransType(TransTypeEnum.RECHARGE_TYPE.getType());
	     accountLs.setTotalBalance(new BigDecimal(account.getTotalBalance()).add(new BigDecimal(amount)).longValue());
	     FinUserAccountLsDao.save(accountLs);
	     log.info(L.b(outOrderNo).bizType("[用户充值]").m("用户充值流水记账成功-->交易后余额:"+accountLs.getTotalBalance()).s());
//		4)平台账户总余额增加金额（充值金额-平台支出手续费），可提现金额增加金额（交易金额-平台手续费金额）、平台支出手续费增加手续费金额（平台和银行交易的手续费金额）
	     Long  addPlatSubAmount = amount - platServiceAmount;
	     FinPlatSubAccountBalanceDao.AddTotalBalanceAndUseAbleBalanceAndPlatServiceAmount(platSubAccount.getId(), addPlatSubAmount,outOrderNo,platServiceAmount);
		 log.info(L.b(outOrderNo).bizType("[用户充值]").m("平台账户变更成功-->平台账户名称:"+platSubAccount.getAccountName()+"平台账户号:"+platSubAccount.getAccountNo()).s());
	     //6)平台商户流水表计入对应流水；（交易类型充值）
	     FinPlatAccountLs  platAccountLs = new FinPlatAccountLs();
	     platAccountLs.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
         platAccountLs.setAccountNo(platSubAccount.getAccountNo());
         platAccountLs.setAccountType(accountType);
         platAccountLs.setMchAccountNo(platSubAccount.getAccountNo());
         platAccountLs.setUseAccountNo(account.getAccountNo());
         platAccountLs.setOutOrderNo(outOrderNo);
         platAccountLs.setOutOrderType(outOrderType);
         platAccountLs.setMerServiceAmount(merServiceAmount);
         platAccountLs.setMerServiceRate(merServiceRate);
         platAccountLs.setPlatServiceAmount(platServiceAmount);
         platAccountLs.setPlatServiceRate(platServiceRate);
         platAccountLs.setTransAmount(amount);
         platAccountLs.setTransType(TransTypeEnum.RECHARGE_TYPE.getType());
         platAccountLs.setTransStatus(TransStatusEnum.NORMAL.getStatus());
         String palteTradeNo =createTradeNo();
         platAccountLs.setTransNo(palteTradeNo);
         platAccountLs.setTransDate(transDate);
         platAccountLs.setTotalBalance(new BigDecimal(platSubAccount.getTotalBalance()).add(new BigDecimal(addPlatSubAmount)).longValue());
         FinPlatAccountLsDao.save(platAccountLs);
         FinPlatAccountLs  platAccountLsExpenses = new FinPlatAccountLs();
         platAccountLsExpenses.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
         platAccountLsExpenses.setAccountNo(platSubAccount.getAccountNo());
         platAccountLsExpenses.setAccountType(accountType);
         platAccountLsExpenses.setMchAccountNo(platSubAccount.getAccountNo());
         platAccountLsExpenses.setUseAccountNo(account.getAccountNo());
         platAccountLsExpenses.setOutOrderNo(outOrderNo);
         platAccountLsExpenses.setOutOrderType(outOrderType);
         platAccountLsExpenses.setMerServiceAmount(merServiceAmount);
         platAccountLsExpenses.setMerServiceRate(merServiceRate);
         platAccountLsExpenses.setPlatServiceAmount(platServiceAmount);
         platAccountLsExpenses.setPlatServiceRate(platServiceRate);
         platAccountLsExpenses.setTransAmount(amount);
         platAccountLsExpenses.setTransType(TransTypeEnum.SERVICE_CHARGE.getType());
         platAccountLsExpenses.setTransStatus(TransStatusEnum.NORMAL.getStatus());
         platAccountLsExpenses.setTransNo(palteTradeNo);
         platAccountLsExpenses.setTransDate(transDate);
         FinPlatAccountLsDao.save(platAccountLsExpenses);
         log.info(L.b(outOrderNo).bizType("[用户充值]").m("平台交易流水记账成功--->交易后余额:"+platAccountLs.getTotalBalance()).s());
         
         return BaseResp.successInstance();
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(new BigDecimal(100L).subtract(new BigDecimal(1l)).longValue());
	}
	
	

}
