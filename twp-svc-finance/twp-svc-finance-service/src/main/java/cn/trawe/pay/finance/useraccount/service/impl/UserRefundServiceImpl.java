package cn.trawe.pay.finance.useraccount.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.FinanceConstants;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.core.CreateTradeNoService;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.IncomeExpensesEnum;
import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountBalanceDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountLsDao;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.plataccount.dao.FinPlatAccountLsDao;
import cn.trawe.pay.finance.plataccount.dao.FinPlatSubAccountBalanceDao;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountLs;
import cn.trawe.pay.finance.plataccount.entity.FinPlatSubAccountBalance;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountLsDao;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserRefundServiceImpl extends BaseServiceImpl {

    @Autowired
    FinMchAccountLsDao finMchAccountLsDao;

    @Autowired
    FinMchAccountBalanceDao finMchAccountBalanceDao;

    @Autowired
    FinUserAccountBalanceDao finUserAccountBalanceDao;

    @Autowired
    FinPlatSubAccountBalanceDao finPlatSubAccountBalanceDao;

    @Autowired
    CreateTradeNoService createTradeNoService;

    @Autowired
    FinPlatAccountLsDao finPlatAccountLsDao;

    @Autowired
    FinUserAccountLsDao finUserAccountLsDao;



    @Transactional(rollbackFor = {Exception.class})
    public BaseResp userRefund(AccountRefundReq request){
    	 if(AccountTypeEnum.WX_ACCOUNT.equals(request.getAccountType())) {
    		 request.setPlatServiceAmount(calculatePlateRateAmount(request.getAmount(),AccountTypeEnum.WX_ACCOUNT));
    		 request.setPlatServiceRate(WX_PLATERATE.toString());
         }
         if(AccountTypeEnum.ALI_ACCOUNT.equals(request.getAccountType())) {
        	 request.setPlatServiceAmount(calculatePlateRateAmount(request.getAmount(),AccountTypeEnum.ALI_ACCOUNT));
        	 request.setPlatServiceRate(ALI_PLATERATE.toString());
         }
         if(AccountTypeEnum.PLAT_ACCOUNT.equals(request.getAccountType())) {
        	 request.setPlatServiceAmount(0L);
        	 request.setPlatServiceRate("0");
         }
        //查询用户退款时对应商户的交易流水
        FinMchAccountLs mchLs=finMchAccountLsDao.getDataByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.INCOME.getType());
        if(mchLs==null){
            throw TwFinanceException.MER_ACCOUNT_Ls_0;
        }
        //如果状态为已退款，则直接返回
        if((TransStatusEnum.REFUND.getStatus())==(mchLs.getTransStatus())){
            throw TwFinanceException.MER_ACCOUNT_REFUNDED_0;
        }
         /**
         * 判断属于哪种退款类型。包括两种场景：
         * 1，商户已经结算的退款。
         * 2，商户未结算的退款
         */
        
        String mchId= request.getMchId();
        //查询商户账户的收入
        FinMchAccountBalance res=finMchAccountBalanceDao.getDataByMerId(mchId,AccountTypeEnum.PLAT_ACCOUNT.getAccount());
//        Long freezeBalance=res.getFreezeBalance();//收入金额
        //当商户交易流水为未结算时，直接从商户收入退款
        if(mchLs.getTransStatus()==TransStatusEnum.NORMAL.getStatus()){
        	log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").m("商户交易流水未结算--->收入退款").s());
            refundByIncome(res,request,mchLs);
        }
        //当商户交易流水为已结算和结算中时，从商户可提现金额退款，若商户可提现金额不足，则提示商户充值
        else if((mchLs.getTransStatus()==TransStatusEnum.CLSD.getStatus())||(mchLs.getTransStatus()==TransStatusEnum.CLSDING.getStatus())){
            //如果商户的可提现金额小于退款金额，则直接返回此次退款失败，让商户充值
            if(res.getUseableBalance()<request.getAmount()){
                throw TwFinanceException.MER_ACCOUNT_NOMONEY_0;
            }else {
            	log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").m("商户交易流水已结算或结算中--->可提现退款").s());
                refundByWithdrawable(res,request,mchLs);
            }
        }else {
            throw TwFinanceException.MER_ACCOUNT_UNKONW_LS_0;
        }
        return BaseResp.successInstance();
    }

    /**
     * 当商户收入大于等于退款金额时，直接从商户收入退款
     * @param request
     * @param accountType
     */
    @Transactional(rollbackFor = {Exception.class})
    public void refundByIncome(FinMchAccountBalance res,AccountRefundReq request,FinMchAccountLs mchLs){
        //减少商户的总金额，收入金额，支出手续费
    	
        Map<String,Object> param1=new HashMap<>();
        param1.put("mchId",request.getMchId());
        param1.put("id",res.getId());
        param1.put("totalBalance",-(mchLs.getTransAmount()-mchLs.getMerServiceAmount()));//总金额
        param1.put("freezeBalance",-(mchLs.getTransAmount()-mchLs.getMerServiceAmount()));//收入金额
        param1.put("useableBalance",0L);//可提现金额
        param1.put("merServiceAmount",-mchLs.getMerServiceAmount());//支出手续费
        param1.put("outOrderNo",request.getOutOrderNo());
        finMchAccountBalanceDao.changeFreezeBalance(param1);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").m("商户账户变更成功--->商户账户号:"+res.getAccountNo()).s());
        //生成商户退款流水
        createMchLs(res,mchLs,request);
        //更改商户交易流水为已退款
        updateMchLs(request);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").m("更新商户流水为已退款").s());
        if("PLA".equals(mchLs.getAccountType())){
            //如果是平台支付 增加用户的总余额，可用余额
            // 如果是第三方支付 用户账户不变  
        	//用户会存在ETC 账户 
        	FinUserAccountBalance account = finUserAccountBalanceDao.getDataByUserIdAndAccountType(request.getUserId(), AccountTypeEnum.PLAT_ACCOUNT.getAccount());
        	if(account==null) {
				//账户不存在
        		log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt("余额支付").m("用户账户不存在").s());
				throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("数据库操作, query 返回空.用户ID:{%s}", request.getUserId());
			}
            finUserAccountBalanceDao.changeUserBalance(account.getId(),request.getAmount(),request.getOutOrderNo());
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt("余额支付").m("用户账户变更成功--->用户账户号:"+account.getAccountNo()).s());
            //更改用户交易流水为已退款
            updateUserLs(request);
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt("余额支付").m("更新用户流水为已退款--->余额支付退款平台账户不变更").s());
//        	生成用户退款流水
            createUserLs(request,account);
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt("余额支付").m("生成用户流水成功").opResult(FinanceConstants.SUCCESS_BUSINESS_CODE_ST).s());
            return;
       }
        //	生成用户退款流水
        createUserLs(request,null);
        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(mchLs.getAccountType());
        //减少平台账户的总金额，收入金额，平台支出手续费
        Map<String,Object> param2=new HashMap<>();
        param2.put("accountType",request.getAccountType().getAccount());
        param2.put("totalBalance",-(request.getAmount()-mchLs.getPlatServiceAmount()));//总金额
        param2.put("useableBalance",0L);//可提现金额
        param2.put("freezeBalance",-(request.getAmount()-mchLs.getPlatServiceAmount()));//收入金额
        param2.put("merServiceAmount",0L);//平台收入手续费
        param2.put("platServiceAmount",-mchLs.getPlatServiceAmount());//平台支出手续费
        param2.put("outOrderNo",request.getOutOrderNo());
        finPlatSubAccountBalanceDao.changePlatBalance(param2);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt(request.getAccountType().getAccount()).m("平台账户变更成功").s());
        //生成平台退款流水
        createPlatLs(request,platSubAccount);
        //更改平台交易流水为已退款
        updatePlatLs(request);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("收入退款").opt(request.getAccountType().getAccount()).opResult(FinanceConstants.SUCCESS_BUSINESS_CODE_ST).m("更新平台流水为已退款").s());
    }

    /**
     * 当商户收入小于退款金额时，从商户可提现金额退款
     * @param request
     * @param accountType
     */
    @Transactional(rollbackFor = {Exception.class})
    public void refundByWithdrawable(FinMchAccountBalance res,AccountRefundReq request,FinMchAccountLs mchLs){
        Long refundMoney=mchLs.getTransAmount()-mchLs.getMerServiceAmount();
        //减少商户的总余额,可提现金额，支出手续费
        Map<String,Object> param1=new HashMap<>();
        param1.put("mchId",request.getMchId());
        param1.put("id",res.getId());
        param1.put("totalBalance",-refundMoney);//总余额
        param1.put("freezeBalance",0L);//收入金额
        param1.put("useableBalance",-refundMoney);//可提现金额
        param1.put("merServiceAmount",-mchLs.getMerServiceAmount());//支出手续费
        param1.put("outOrderNo",request.getOutOrderNo());
        finMchAccountBalanceDao.changeFreezeBalance(param1);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").m("商户账户变更成功--->商户账户号:"+res.getAccountNo()).s());
        //生成商户退款流水
        createMchLs(res,mchLs,request);
        //更改商户交易流水为已退款
        updateMchLs(request);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").m("更新商户流水为已退款").s());
        if("PLA".equals(mchLs.getAccountType())){
        //如果是平台支付 增加用户的总余额，可用余额
            // 如果是第三方支付 用户账户不变
        	//根据userId 查询账户号
        	FinUserAccountBalance account = finUserAccountBalanceDao.getDataByUserIdAndAccountType(request.getUserId(), AccountTypeEnum.PLAT_ACCOUNT.getAccount());
        	if(account==null) {
				//账户不存在
        		log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").m("用户账户不存在").s());
				throw  TwFinanceException.ACCOUNT_RESULT_0.newInstance("数据库操作, query 返回空.用户ID:{%s}", request.getUserId());
			}
            finUserAccountBalanceDao.changeUserBalance(account.getId(),request.getAmount(),request.getOutOrderNo());
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").m("用户账户变更成功--->用户账户号:"+account.getAccountNo()).s());
          
            //更改用户交易流水为已退款
            updateUserLs(request);
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").opt("余额支付").m("更新用户流水为已退款--->余额支付退款平台账户不变更").s());
          //生成用户退款流水
            createUserLs(request,account);
            log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").opt("余额支付").m("生成用户流水成功").opResult(FinanceConstants.SUCCESS_BUSINESS_CODE_ST).s());
            return;
        }
        //生成用户退款流水
        createUserLs(request,null);
        FinPlatSubAccountBalance platSubAccount = FinPlatSubAccountBalanceDao.getData(mchLs.getAccountType());
        //减少平台总余额，可提现金额，平台支出手续费
        Map<String,Object> param2=new HashMap<>();
        param2.put("accountType",request.getAccountType().getAccount());
        param2.put("totalBalance",-(request.getAmount()-mchLs.getPlatServiceAmount()));//总余额
        param2.put("freezeBalance",0L);//收入金额
        param2.put("useableBalance",-(request.getAmount()-mchLs.getPlatServiceAmount()));//可提现金额
        param2.put("merServiceAmount",0L);//平台收入手续费
        param2.put("platServiceAmount",-mchLs.getPlatServiceAmount());//平台支出手续费
        param2.put("outOrderNo",request.getOutOrderNo());
        finPlatSubAccountBalanceDao.changePlatBalance(param2);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").opt(request.getAccountType().getAccount()).m("平台账户变更成功").s());
        //生成平台退款流水
        createPlatLs(request,platSubAccount);
        //更改平台交易流水为已退款
        updatePlatLs(request);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").subBizType("可提现金额退款").opt(request.getAccountType().getAccount()).opResult(FinanceConstants.SUCCESS_BUSINESS_CODE_ST).m("更新平台流水为已退款").s());
        
    }

    /**
     * 生成平台退款流水
     * @param request
     */
    public void createPlatLs(AccountRefundReq request,FinPlatSubAccountBalance plateAccount){
        //查询用户退款时对应平台的交易流水
        FinPlatAccountLs platLs=finPlatAccountLsDao.getDataByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.INCOME.getType());
        FinPlatAccountLs newPlatls=new FinPlatAccountLs();
        newPlatls.setIncomeExpenses(IncomeExpensesEnum.EXPENSES.getType());
        newPlatls.setAccountNo(platLs.getAccountNo());
        newPlatls.setAccountType(platLs.getAccountType());
        newPlatls.setCreateTime(new Date());
        newPlatls.setMchAccountNo(platLs.getMchAccountNo());
        newPlatls.setMerServiceAmount(platLs.getMerServiceAmount());
        newPlatls.setMerServiceRate(platLs.getMerServiceRate());
        newPlatls.setOutOrderNo(request.getOutRefundOrderNo());
        newPlatls.setOutOrderType(request.getOutRefundOrderType());
        newPlatls.setTransDate(request.getOutRefundOrderDate());
        newPlatls.setPlatServiceAmount(platLs.getPlatServiceAmount());
        newPlatls.setPlatServiceRate(platLs.getPlatServiceRate());
        newPlatls.setTransAmount(platLs.getTransAmount());
        String createTradeNo = createTradeNoService.createTradeNo(new Date(),"R",9);
        newPlatls.setTransNo(createTradeNo);
        newPlatls.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        newPlatls.setTransType(TransTypeEnum.REFOUND_TYPE.getType());
//        newPlatls.setUpdateTime();
        newPlatls.setUseAccountNo(platLs.getUseAccountNo());
        newPlatls.setTotalBalance(plateAccount.getTotalBalance()-request.getAmount()+request.getPlatServiceAmount());
        finPlatAccountLsDao.save(newPlatls);
        FinPlatAccountLs newPlatlsIncome=new FinPlatAccountLs();
        newPlatlsIncome.setAccountNo(platLs.getAccountNo());
        newPlatlsIncome.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
        newPlatlsIncome.setAccountType(platLs.getAccountType());
        newPlatlsIncome.setCreateTime(new Date());
        newPlatlsIncome.setMchAccountNo(platLs.getMchAccountNo());
        newPlatlsIncome.setOutOrderNo(request.getOutRefundOrderNo());
        newPlatlsIncome.setOutOrderType(request.getOutRefundOrderType());
        newPlatlsIncome.setTransDate(request.getOutRefundOrderDate());
        newPlatlsIncome.setPlatServiceAmount(platLs.getPlatServiceAmount());
        newPlatlsIncome.setPlatServiceRate(platLs.getPlatServiceRate());
        newPlatlsIncome.setTransAmount(platLs.getTransAmount());
        newPlatlsIncome.setTransNo(createTradeNo);
        newPlatlsIncome.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        newPlatlsIncome.setTransType(TransTypeEnum.SERVICE_CHARGE_REFUND.getType());
        newPlatlsIncome.setMemo(TransTypeEnum.SERVICE_CHARGE_REFUND.getDesc()+"|"+request.getOutRefundOrderNo());
        newPlatlsIncome.setUseAccountNo(platLs.getUseAccountNo());
        finPlatAccountLsDao.save(newPlatlsIncome);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").m("平台交易流水记账成功--->账户号:"+platLs.getAccountNo()+"--->交易后余额:"+newPlatls.getTotalBalance()).s());
    }

    /**
     * 生成用户退款流水
     * @param request
     */
    public void createUserLs(AccountRefundReq request,FinUserAccountBalance account){
        //查询用户退款时对应用户的交易流水
        FinUserAccountLs userLs=finUserAccountLsDao.getDataByOutOrderNo(request.getOutOrderNo(),request.getOutOrderType(),request.getAmount());
        FinUserAccountLs newUserls=new FinUserAccountLs();
        newUserls.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
        newUserls.setAccountNo(userLs.getAccountNo());
        newUserls.setAccountType(userLs.getAccountType());
        newUserls.setCreateTime(new Date());
        newUserls.setMchAccountNo(userLs.getMchAccountNo());
        newUserls.setMerServiceAmount(userLs.getMerServiceAmount());
        newUserls.setMerServiceRate(userLs.getMerServiceRate());
        newUserls.setOutOrderNo(request.getOutRefundOrderNo());
        newUserls.setOutOrderType(request.getOutRefundOrderType());
        newUserls.setTransDate(request.getOutRefundOrderDate());
        newUserls.setPlatServiceAmount(userLs.getPlatServiceAmount());
        newUserls.setPlatServiceRate(userLs.getPlatServiceRate());
        newUserls.setTransAmount(userLs.getTransAmount());
        newUserls.setTransNo(createTradeNoService.createTradeNo(new Date(),"R",9));
        newUserls.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        newUserls.setTransType(TransTypeEnum.REFOUND_TYPE.getType());
        newUserls.setMemo(TransTypeEnum.REFOUND_TYPE.getDesc()+"|"+request.getOutRefundOrderNo());
        if("PLA".equals(userLs.getAccountType())){
            newUserls.setTotalBalance(account.getTotalBalance()+userLs.getTransAmount());
        }else {
            newUserls.setTotalBalance(0L);
        }
        
        finUserAccountLsDao.save(newUserls);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").m("用户退款流水记账成功--->账户号:"+userLs.getAccountNo()+"--->交易后余额:"+newUserls.getTotalBalance()).s());
    }

    /**
     * 生成商户退款流水
     * @param request
     */
    public void createMchLs(FinMchAccountBalance  res,FinMchAccountLs mchLs,AccountRefundReq request){
        //查询商户退款时对应商户的交易流水
        //FinMchAccountLs mchLs=finMchAccountLsDao.getDataByOutOrderNo(request.getOutOrderNo(),request.getOutOrderType(),request.getAmount());
        FinMchAccountLs newMchls=new FinMchAccountLs();
        newMchls.setIncomeExpenses(IncomeExpensesEnum.INCOME.getType());
        newMchls.setAccountNo(mchLs.getAccountNo());
        newMchls.setAccountType(mchLs.getAccountType());
        newMchls.setCreateTime(new Date());
        newMchls.setMerServiceAmount(mchLs.getMerServiceAmount());
        newMchls.setMerServiceRate(mchLs.getMerServiceRate());
        newMchls.setOutOrderNo(request.getOutRefundOrderNo());
        newMchls.setOutOrderType(request.getOutRefundOrderType());
        newMchls.setTransDate(request.getOutRefundOrderDate());
        newMchls.setPlatServiceAmount(mchLs.getPlatServiceAmount());
        newMchls.setPlatServiceRate(mchLs.getPlatServiceRate());
        newMchls.setTransAmount(mchLs.getTransAmount());
        String createTradeNo = createTradeNoService.createTradeNo(new Date(),"R",9);
        newMchls.setTransNo(createTradeNo);
        newMchls.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        newMchls.setTransType(TransTypeEnum.REFOUND_TYPE.getType());
        newMchls.setUseAccountNo(mchLs.getUseAccountNo());
        newMchls.setTotalBalance(res.getTotalBalance()-mchLs.getTransAmount()+mchLs.getMerServiceAmount());
        finMchAccountLsDao.save(newMchls);
        FinMchAccountLs newMchlsExpenses=new FinMchAccountLs();
        newMchlsExpenses.setAccountNo(mchLs.getAccountNo());
        newMchlsExpenses.setAccountType(mchLs.getAccountType());
        newMchlsExpenses.setCreateTime(new Date());
        newMchlsExpenses.setMerServiceAmount(mchLs.getMerServiceAmount());
        newMchlsExpenses.setMerServiceRate(mchLs.getMerServiceRate());
        newMchlsExpenses.setOutOrderNo(request.getOutRefundOrderNo());
        newMchlsExpenses.setOutOrderType(request.getOutRefundOrderType());
        newMchlsExpenses.setTransDate(request.getOutRefundOrderDate());
        newMchlsExpenses.setTransAmount(mchLs.getTransAmount());
        newMchlsExpenses.setTransNo(createTradeNo);
        newMchlsExpenses.setTransStatus(TransStatusEnum.NORMAL.getStatus());
        newMchlsExpenses.setTransType(TransTypeEnum.SERVICE_CHARGE_REFUND.getType());
        newMchlsExpenses.setUseAccountNo(mchLs.getUseAccountNo());
        newMchlsExpenses.setMemo(TransTypeEnum.SERVICE_CHARGE_REFUND.getDesc()+"|"+request.getOutRefundOrderNo());
        finMchAccountLsDao.save(newMchlsExpenses);
        log.info(L.b(request.getOutRefundOrderNo()).bizType("[退款]").m("商户退款流水记账成功---->商户号:"+mchLs.getAccountNo()+"-->交易后余额:"+newMchls.getTotalBalance()).s());
    }

    /**
     * 更改用户流水状态为已退款
     * @param request
     */
    public void updateUserLs(AccountRefundReq request){
        int update = finUserAccountLsDao.updateTransStatusByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.EXPENSES.getType());
        if(update <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(request));
		}
    }

    /**
     * 更改平台流水状态为已退款
     * @param request
     */
    public void updatePlatLs(AccountRefundReq request){
        int update = finPlatAccountLsDao.updateTransStatusByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.INCOME.getType());
        if(update <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(request));
		}
        int updateExpenses = finPlatAccountLsDao.updateTransStatusByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.EXPENSES.getType());
        if(updateExpenses <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(request));
		}
    }

    /**
     * 更改商户流水状态为已退款
     * @param request
     */
    public void updateMchLs(AccountRefundReq request){
        int update = finMchAccountLsDao.updateTransStatusByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.INCOME.getType());
        if(update <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(request));
		}
        int updateExpenses = finMchAccountLsDao.updateTransStatusByOutOrderNo(request.getOutOrderNo(),IncomeExpensesEnum.EXPENSES.getType());
        if(updateExpenses <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(request));
		}
    }
}


