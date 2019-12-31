package cn.trawe.pay.finance.task.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.MchSettleBillStatusEnum;
import cn.trawe.pay.finance.enums.SettleBatchStatusEnum;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountBalanceDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountLsDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountSettleBatchDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchSettleBillDao;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountSettleBatchTb;
import cn.trawe.pay.finance.meraccount.entity.FinMchSettleBill;
import cn.trawe.pay.finance.task.utils.DateUtil;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountLsDao;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author jianjun.chai
 *
 */
@Service
@Slf4j
public class BussinessSettleService extends BaseServiceImpl{
	
	
	
	 @Autowired
	 private SettleService settleService;
	 
    // 商户账户交易流水表
     @Autowired
     private FinMchAccountLsDao finMchAccountLsDao;
     // 用户账户交易流水表
     @Autowired
     private FinUserAccountLsDao finUserAccountLsDao;
     
   
    
    
	
	/**
     * 数据是用mysql聚合函数算出来的。
     * 柴总说，不用使用id，所以用这个方法。
     * 针对单独商户盘账
     *
     * @param accountNo
     * @param beginTime
     * @param endTime
     * @throws Exception 
     */
    @Transactional(rollbackFor = Exception.class)
    public void settle(String accountNo, Date beginTime, Date endTime) throws Exception {

    	     //是否存在已经正常清分的批次
    	    boolean settleBatchIsSuccess = settleService.settleBatchIsSuccess(accountNo,beginTime);
    	    if(settleBatchIsSuccess) {
    	        log.info(L.b(accountNo).bizType("清分").m("存在成功的清分批次...账单开始时间--->"+DateUtil.DateToStr(beginTime)+"账单结束时间--->"+DateUtil.DateToStr(endTime)).opResult("RETURN").s());
    	        return;
    	    }
    		log.info(L.b(accountNo).bizType("清分").m("开始清分...账单开始时间--->"+DateUtil.DateToStr(beginTime)+"账单结束时间--->"+DateUtil.DateToStr(endTime)).s());
            // 结算账单列表,可能同时包含汇总,支付宝,微信,平台
            List<FinMchSettleBill> finMchSettleBillList = new ArrayList<>();

            // 结算批次表实体
            FinMchAccountSettleBatchTb finMchAccountSettleBatchTb = new FinMchAccountSettleBatchTb();
            finMchAccountSettleBatchTb.setBatchVersion(beginTime);
            finMchAccountSettleBatchTb.setBatchNo(createTradeNo());
            finMchAccountSettleBatchTb.setAccountNo(accountNo);
            finMchAccountSettleBatchTb.setBeginDate(beginTime);
            finMchAccountSettleBatchTb.setEndDate(endTime);
            
            // 查询用户表流水聚合数据
            List<Map<String, Object>> userList = finUserAccountLsDao.queryCountAndTotalTransAmount(accountNo, beginTime, endTime);
            // 查询商户表流水聚合数据
            List<Map<String, Object>> mchList = finMchAccountLsDao.queryCountAndTotalTransAmount(accountNo, beginTime, endTime);

            if (userList.isEmpty() && mchList.isEmpty()) {
                // 这个商户 ,今天没啥交易
                FinMchSettleBill finMchSettleBill = new FinMchSettleBill();
                finMchSettleBill.setAccountNo(accountNo);
                finMchSettleBill.setAccountType(AccountTypeEnum.ALL_ACCOUNT.getAccount());
                finMchSettleBill.setBeginDate(beginTime);
                finMchSettleBill.setEndDate(endTime);
                finMchSettleBill.setIncomeCount((long) 0);
                finMchSettleBill.setIncomeAmount((long) 0);
                finMchSettleBill.setExpandAmount((long) 0);
                finMchSettleBill.setTransProfit((long) 0);
                finMchSettleBill.setBillStatus(MchSettleBillStatusEnum.BACKGROUND_PROCESSING.getStatus());
                finMchSettleBill.setBatchNo(finMchAccountSettleBatchTb.getBatchNo());
                finMchSettleBillList.add(finMchSettleBill);

                finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.SUCCESS.getStatus());
                finMchAccountSettleBatchTb.setReason("结算周期内该商户无交易");
                try {
                	settleService.saveSettleBillAndBatch(finMchSettleBillList, finMchAccountSettleBatchTb);
                } catch (Exception e) {
                    log.error("商户{}没有交易信息,保存结算账单和批次失败{}", accountNo, e.getMessage(),e);
                    finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.UPDATE_FAIL.getStatus());
                    finMchAccountSettleBatchTb.setReason(String.format("商户结算周期内%s无交易,入库失败%s", accountNo, e.getMessage().substring(0, 400)));
                    settleService.saveSettleBatchCatchException(finMchAccountSettleBatchTb);
                    throw e;
                }
                
                log.info(L.b(accountNo).bizType("清分").m("该商户无交易").s());
                return;
            }

            if (mchList.size() != userList.size()) {
                // list的长度不一样
                log.error("商户{},商户交易流水表支付类型有{}种,数据是{},用户交易流水表支付类型有{}种,数据是{},数量不一致", accountNo, mchList.size(), mchList, userList.size(), userList);
                finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
                finMchAccountSettleBatchTb.setReason(String.format("商户交易流水表支付类型查询出%d条,用户交易流水表支付类型查出%d条,数据不一致", mchList.size(), userList.size()));
                settleService.saveSettleBatchCatchException(finMchAccountSettleBatchTb);
              //需要有报警
                log.error(L.b(accountNo).bizType("清分").m(String.format("商户交易流水表支付类型查询出%d条,用户交易流水表支付类型查出%d条,数据不一致", mchList.size(), userList.size())).s());
                return;
            }
            Map<String, FinMchSettleBill> userMap = listToMap(userList);
            Map<String, FinMchSettleBill> mchMap = listToMap(mchList);
            if (userMap.size() != mchMap.size()) {
                // map长度不一致
                log.error("商户{},商户交易流水表支付类型有{}种,用户交易流水表支付类型有{}种, 数量不一致", accountNo, mchMap.size(), userMap.size());
                finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
                finMchAccountSettleBatchTb.setReason(String.format("商户交易流水表支付类型有%d种%s,用户交易流水表支付类型有%d种%s,数量不一致,"
                        , mchMap.size(), mchMap.keySet(), userMap.size(), userMap.keySet()));
                settleService.saveSettleBatchCatchException(finMchAccountSettleBatchTb);
              //需要有报警
                log.error(L.b(accountNo).bizType("清分").m(String.format("商户交易流水表支付类型有%d种%s,用户交易流水表支付类型有%d种%s,数量不一致,"
                        , mchMap.size(), mchMap.keySet(), userMap.size(), userMap.keySet())).s());
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();

            // 账单汇总
            FinMchSettleBill finMchSettleBillAll = new FinMchSettleBill();
            // 填补 入库账单表需要的参数
            finMchSettleBillAll.setAccountType(AccountTypeEnum.ALL_ACCOUNT.getAccount());
            finMchSettleBillAll.setAccountNo(accountNo);
            finMchSettleBillAll.setIncomeCount((long) 0);
            finMchSettleBillAll.setIncomeAmount((long) 0);
            finMchSettleBillAll.setExpandAmount((long) 0);
            finMchSettleBillAll.setBillStatus(MchSettleBillStatusEnum.BACKGROUND_PROCESSING.getStatus());
            finMchSettleBillAll.setTransProfit((long) 0);       // 柴总说，这个先不维护
            finMchSettleBillAll.setBeginDate(beginTime);
            finMchSettleBillAll.setEndDate(endTime);
            finMchSettleBillAll.setBatchNo(finMchAccountSettleBatchTb.getBatchNo());
            // 填补 入库账单表需要的参数 结束

            for (String key : mchMap.keySet()) {
                FinMchSettleBill finMchSettleBillMch = mchMap.get(key);
                FinMchSettleBill finMchSettleBillUser = userMap.get(key);
                if (finMchSettleBillUser == null) {
                    stringBuilder.append("支付类型:").append(key).append("在用户交易流水表不存在该支付类型的交易").append(System.lineSeparator());
                    continue;
                }
                if (!finMchSettleBillMch.getIncomeCount().equals(finMchSettleBillUser.getIncomeCount())) {
                    stringBuilder.append("支付类型:").append(key).append("商户表消费笔数:").append(finMchSettleBillMch.getIncomeCount())
                            .append("用户交易流水表交易笔数:").append(finMchSettleBillUser.getIncomeCount()).append(System.lineSeparator());
                    //continue;
                }
                if (!finMchSettleBillMch.getIncomeAmount().equals(finMchSettleBillUser.getIncomeAmount())) {
                    stringBuilder.append("支付类型:").append(key).append("商户交易流水表交易金额:").append(finMchSettleBillMch.getIncomeAmount())
                            .append("用户交易流水表交易金额:").append(finMchSettleBillUser.getIncomeAmount()).append(System.lineSeparator());
                    //continue;
                }
                if (!finMchSettleBillMch.getExpandAmount().equals(finMchSettleBillUser.getExpandAmount())) {
                    stringBuilder.append("支付类型:").append(key).append("商户交易流水表商户手续费支出:").append(finMchSettleBillMch.getExpandAmount())
                            .append("用户交易流水表商户手续费支出:").append(finMchSettleBillUser.getExpandAmount()).append(System.lineSeparator());
                    continue;
                }
                // 填补这个支付类型入库账单表需要的参数
                finMchSettleBillMch.setAccountNo(accountNo);
                finMchSettleBillMch.setBeginDate(beginTime);
                finMchSettleBillMch.setEndDate(endTime);
                finMchSettleBillMch.setBillStatus(MchSettleBillStatusEnum.BACKGROUND_PROCESSING.getStatus());
                finMchSettleBillMch.setTransProfit((long) 0);        // 柴总说，这个先不维护
                finMchSettleBillMch.setBatchNo(finMchAccountSettleBatchTb.getBatchNo());
                // 这种支付类型,对账通过,计算汇总
                finMchSettleBillAll.setIncomeCount(finMchSettleBillAll.getIncomeCount() + finMchSettleBillMch.getIncomeCount());
                finMchSettleBillAll.setIncomeAmount(finMchSettleBillAll.getIncomeAmount() + finMchSettleBillMch.getIncomeAmount());
                finMchSettleBillAll.setExpandAmount(finMchSettleBillAll.getExpandAmount() + finMchSettleBillMch.getExpandAmount());
               
                log.info(L.b(accountNo).bizType("清分").opt(key).opResult("SUCCESS").m(JSON.toJSONString(finMchSettleBillMch)).s());
            }
            if (stringBuilder.length() != 0) {
                // 对账失败了
                finMchAccountSettleBatchTb.setReason(stringBuilder.toString());
                finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
                settleService.saveSettleBatchCatchException(finMchAccountSettleBatchTb);   
                log.error(L.b(accountNo).bizType("清分").opt(accountNo).m("结算周期存在交易但是对账失败--->"+stringBuilder.toString()).s());
                return;
            }
            // 对账成功
            finMchSettleBillList.addAll(new ArrayList<>(mchMap.values()));
            finMchAccountSettleBatchTb.setReason("清分成功");
            finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.SUCCESS.getStatus());
            try {
            	settleService.settleSuccess(finMchSettleBillList, finMchAccountSettleBatchTb, finMchSettleBillAll);
            	log.info(L.b(accountNo).bizType("清分").opt(AccountTypeEnum.ALL_ACCOUNT.getAccount()).opResult("SUCCESS").m(JSON.toJSONString(finMchSettleBillAll)).s());
            } catch (Exception e) {
                log.error("商户{}对账成功,入库结算账单失败{}", accountNo, e.getMessage(),e);
                finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.UPDATE_FAIL.getStatus());
                finMchAccountSettleBatchTb.setReason(String.format("商户结算周期内%s对账成功,入库结算账单失败%s", accountNo, e.getMessage().substring(0, 400)));
                settleService.saveSettleBatchCatchException(finMchAccountSettleBatchTb);
                throw e;
            }
            log.info(L.b(accountNo).bizType("清分").m("清分结束...账单开始时间--->"+DateUtil.DateToStr(beginTime)+"账单结束时间--->"+DateUtil.DateToStr(endTime)).opResult("SUCCESS").s());
    	
    }
    
    /**
     * 把查询的聚合结果数组，转换成map
     *
     * @param list
     * @return
     */
    private Map<String, FinMchSettleBill> listToMap(List<Map<String, Object>> list) {
        Map<String, FinMchSettleBill> result = new HashMap<>();
        for (Map<String, Object> map : list) {
            String accountType = (String) map.get("accountType");
            FinMchSettleBill finMchSettleBill = new FinMchSettleBill();
            finMchSettleBill.setIncomeAmount(((BigDecimal) map.get("transAmountSum")).longValue());
            finMchSettleBill.setExpandAmount(((BigDecimal) map.get("merServiceAmountSum")).longValue());
            finMchSettleBill.setIncomeCount((long) map.get("count"));
            finMchSettleBill.setAccountType(accountType);
            result.put(accountType, finMchSettleBill);
        }
        return result;
    }
    
    
    
    
    
    
    
    /**
     * 此方法暂时不用
     * 查询出所有选项，java代码计算。这样可以用id。
     * 针对单独商户盘账
     *
     * @param accountNo 商户编号
     * @throws Exception 
     */
//    @Transactional(rollbackFor = Exception.class)
//    @Deprecated
//    public void settle2(String accountNo, Date beginTime, Date endTime) throws Exception {
//        // 查询 商户流水总金额。
//        List<FinMchAccountLs> finMchAccountLsList = finMchAccountLsDao.queryNoSettleLs(accountNo, beginTime, endTime);
//        List<FinUserAccountLs> finUserAccountLsList = finUserAccountLsDao.queryNoSettleLs(accountNo, beginTime, endTime);
//        if (finMchAccountLsList == null) {
//            log.warn("查询到的商户{}流水为空", accountNo);
//            return;
//        }
//        if (finUserAccountLsList == null) {
//            log.warn("查询商户{}用户流水为空", accountNo);
//            return;
//        }
//
//        // 结算账单列表,可能同时包含汇总,支付宝,微信,平台
//        List<FinMchSettleBill> finMchSettleBillList = new ArrayList<>();
//
//        // 结算批次表实体
//        FinMchAccountSettleBatchTb finMchAccountSettleBatchTb = new FinMchAccountSettleBatchTb();
//        finMchAccountSettleBatchTb.setBatchVersion(beginTime);
//        finMchAccountSettleBatchTb.setAccountNo(accountNo);
//
//        if (finMchAccountLsList.isEmpty() && finUserAccountLsList.isEmpty()) {
//            // 这个商户,今天没啥交易
//            FinMchSettleBill finMchSettleBill = new FinMchSettleBill();
//            finMchSettleBill.setAccountNo(accountNo);
//            finMchSettleBill.setAccountType(AccountTypeEnum.ALL_ACCOUNT.getAccount());
//            finMchSettleBill.setBeginDate(beginTime);
//            finMchSettleBill.setEndDate(endTime);
//            finMchSettleBill.setIncomeAmount((long) 0);
//            finMchSettleBill.setExpandAmount((long) 0);
//            finMchSettleBill.setTransProfit((long) 0);
//            finMchSettleBill.setBillStatus("0");
//
//            finMchSettleBillList.add(finMchSettleBill);
//
//            finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.SUCCESS.getStatus());
//            finMchAccountSettleBatchTb.setReason("该商户今天没有交易流水");
//            try {
//                saveSettleBillAndBatch(finMchSettleBillList, finMchAccountSettleBatchTb);
//            } catch (Exception e) {
//                log.error("商户{}没有交易信息,保存结算账单和批次失败{}", accountNo, e.getMessage());
//                e.printStackTrace();
//            }
//            return;
//        }
//        // 柴老板说,不需要对每条流水,只需要对总金额
//        if (finMchAccountLsList.size() != finUserAccountLsList.size()) {
//            log.warn("查询商户{}交易流水{}条,用户表交易流水{}条,数量不一致", accountNo, finMchAccountLsList.size(), finUserAccountLsList.size());
//            finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
//            finMchAccountSettleBatchTb.setReason(String.format("商户表交易流水%d条,用户表交易流水%d条,数量不一致"
//                    , finMchAccountLsList.size(), finUserAccountLsList.size()));
//            saveSettleBatchCatchException(finMchAccountSettleBatchTb);
//            return;
//        }
//        Map<String, long[]> mchMoney = calMchMoney(finMchAccountLsList);
//        Map<String, long[]> userMoney = calUserMoney(finUserAccountLsList);
//        if (mchMoney.size() != userMoney.size()) {
//            log.warn("商户{},商户消费类型有{}种,用户表消费类型有{}种,数量不一致", accountNo, mchMoney.size(), userMoney.size());
//            finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
//            finMchAccountSettleBatchTb.setReason(String.format("商户消费类型有%d种%s,用户表消费类型有%d种%s,数量不一致,"
//                    , mchMoney.size(), mchMoney.keySet(), userMoney.size(), userMoney.keySet()));
//            saveSettleBatchCatchException(finMchAccountSettleBatchTb);
//            return;
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String key : mchMoney.keySet()) {
//            long[] mchResult = mchMoney.get(key);
//            long[] userResult = userMoney.get(key);
//            if (userResult == null) {
//                stringBuilder.append("支付类型").append(key).append("在用户消费表不存在").append(",");
//                continue;
//            }
//            if (mchResult[0] != userResult[0]) {
//                stringBuilder.append(key).append("商户表消费总金额").append(mchResult[0])
//                        .append("用户表消费总金额").append(userResult[0]).append(",");
//            }
//            if (mchResult[1] != userResult[1]) {
//                stringBuilder.append(key).append("商户表退款总金额").append(mchResult[0])
//                        .append("用户表退款总金额").append(userResult[0]).append(",");
//            }
//        }
//        if (stringBuilder.length() != 0) {
//            // 对账失败
//            finMchAccountSettleBatchTb.setStatus(SettleBatchStatusEnum.COMPARE_BILL_FAIL.getStatus());
//            finMchAccountSettleBatchTb.setReason(stringBuilder.toString());
//            saveSettleBatchCatchException(finMchAccountSettleBatchTb);
//            return;
//        }
//        // 对账成功
//        System.out.println(stringBuilder.toString());
//    }

}
