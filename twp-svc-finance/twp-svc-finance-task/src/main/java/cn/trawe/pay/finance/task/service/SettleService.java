package cn.trawe.pay.finance.task.service;

import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.ReviewReq;
import cn.trawe.pay.finance.enums.*;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountBalanceDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountSettleBatchDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountLsDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchSettleBillDao;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountSettleBatchTb;
import cn.trawe.pay.finance.meraccount.entity.FinMchSettleBill;
import cn.trawe.pay.finance.task.utils.DateUtil;
import cn.trawe.pay.finance.task.utils.RandomUtil;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountLsDao;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 盘账
 *
 * @author liguangyan
 * @date 2019/11/27 19:05
 */

@Service
@Slf4j
public class SettleService {
   
    // 账单插入人默认数据
    @Value("${finance.task.settleBillOperator}")
    private String settleBillOperator;
    // 商户清算批次表
    @Autowired
    private FinMchAccountSettleBatchDao finMchAccountSettleBatchDao;
    // 商户总账户信息表
    @Autowired
    private FinMchAccountBalanceDao finMchAccountBalanceDao;
    // 商户账户交易流水表
    @Autowired
    private FinMchAccountLsDao finMchAccountLsDao;
    // 用户账户交易流水表
    @Autowired
    private FinUserAccountLsDao finUserAccountLsDao;
    // 结算账单表
    @Autowired
    private FinMchSettleBillDao finMchSettleBillDao;

   

    
    public boolean settleBatchIsSuccess(String accountNo,Date batchVersion) {
    	
    	List<Object> params = new ArrayList<Object>();
    	params.add(accountNo);
    	params.add(batchVersion);
    	FinMchAccountSettleBatchTb find = finMchAccountSettleBatchDao.findOne("where account_no = ? and batch_version = ?  and status =1", params);
    	if(find!=null) {
    		return true;
    	}
    	else {
    		return false;
    	}
    	
    }
    

    /**
     * 清分成功
     */
    public void settleSuccess(List<FinMchSettleBill> finMchSettleBillList
            , FinMchAccountSettleBatchTb finMchAccountSettleBatchTb
            , FinMchSettleBill finMchSettleBillAll) throws Exception {
        finMchSettleBillList.add(finMchSettleBillAll);
        // 保存账单和批次
        saveSettleBillAndBatch(finMchSettleBillList, finMchAccountSettleBatchTb);
        // 更新流水状态
        //TODO  开发环境先不更新流水状态
//        updateLsStatus(finMchSettleBillAll.getIncomeCount(), finMchSettleBillAll.getAccountNo(),
//                finMchSettleBillAll.getBeginDate(), finMchSettleBillAll.getEndDate(),
//                TransStatusEnum.CLSDING.getStatus(), TransStatusEnum.NORMAL.getStatus());
    }

    /**
     * 更新流水状态
     *
     * @param count          查询到的要清分的流水的个数
     * @param beginTime      要更新的流水的开始时间
     * @param endTime        要更新的流水的结束时间
     * @param newTransStatus 要更新的状态
     * @param oldTransStatus 旧状态
     */
    public void updateLsStatus(Long count, String accountNo, Date beginTime, Date endTime, int newTransStatus, int oldTransStatus) throws Exception {
        int countMch = finMchAccountLsDao.updateLsStatus(accountNo, newTransStatus, oldTransStatus, beginTime, endTime);
        if (count != countMch) {
            throw new Exception(String.format("商户%s,商户流水表,查询到的流水数据是%d条,更新状态的流水是%d条", accountNo, count, countMch));
        }
        int countUser = finUserAccountLsDao.updateLsStatus(accountNo, newTransStatus, oldTransStatus, beginTime, endTime);
        if (count != countUser) {
            throw new Exception(String.format("商户%s,用户流水表,查询到的流水数据是%d条,更新状态的流水是%d条", accountNo, count, countUser));
        }
    }

    

    /**
     * 同时保存结算账单和批次
     */
    public void saveSettleBillAndBatch(List<FinMchSettleBill> finMchSettleBillList
            , FinMchAccountSettleBatchTb finMchAccountSettleBatchTb) throws Exception {
        saveSettleBill(finMchSettleBillList);
        saveSettleBatch(finMchAccountSettleBatchTb);
    }

    /**
     * 保存批次号,捕获异常信息并日志
     * @throws Exception 
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW )
    public void saveSettleBatchCatchException(FinMchAccountSettleBatchTb finMchAccountSettleBatchTb) throws Exception {
        try {
            saveSettleBatch(finMchAccountSettleBatchTb);
        } catch (Exception e) {
            log.error("保存清算数据{},批次失败{}", finMchAccountSettleBatchTb, e.getMessage(),e);
            throw e ;
        }
    }

    /**
     * 保存对账批次结果
     */
    public void saveSettleBatch(FinMchAccountSettleBatchTb finMchAccountSettleBatchTb) throws Exception {
        Date date = new Date();
        finMchAccountSettleBatchTb.setCreateTime(date);
        finMchAccountSettleBatchTb.setUpdateTime(date);
        int save = finMchAccountSettleBatchDao.save(finMchAccountSettleBatchTb);
        if (save <= 0) {
            log.warn("插入{}条批次{}", save, finMchAccountSettleBatchTb);
            throw new Exception(String.format("插入%d条批次%s", save, finMchAccountSettleBatchTb));
        }
    }

    /**
     * 保存结算账单,每次可能插入1-4条账单,微信,支付宝,平台,汇总
     */
    public void saveSettleBill(List<FinMchSettleBill> finMchSettleBillList) throws Exception {
        for (FinMchSettleBill finMchSettleBill : finMchSettleBillList) {
            Date date = new Date();
            finMchSettleBill.setCreateTime(date);
            finMchSettleBill.setUpdateTime(date);
            finMchSettleBill.setUpdateOperator(settleBillOperator);
            int save = finMchSettleBillDao.save(finMchSettleBill);
            if (save <= 0) {
                log.warn("插入{}条账单{}", save, finMchSettleBill);
                throw new Exception(String.format("插入%d条账单%s", save, finMchSettleBill));
            }
        }
    }

    /**
     * 计算商户相关金额
     *
     * @param finMchAccountLsList 要计算的流水
     * @return
     */
    private Map<String, long[]> calMchMoney(List<FinMchAccountLs> finMchAccountLsList) {
        Map<String, long[]> map = new HashMap<>();
        for (FinMchAccountLs finMchAccountLs : finMchAccountLsList) {
            String accountType = finMchAccountLs.getAccountType();
            long[] payType = map.get(accountType);
            if (payType == null) {
                payType = new long[]{0, 0};
            }
            if (TransTypeEnum.CONSUME_TYPE.getType().equals(finMchAccountLs.getTransType())) {
                // 消费
                payType[0] += finMchAccountLs.getTransAmount();
                map.put(accountType, payType);
                continue;
            }
            if (TransTypeEnum.REFOUND_TYPE.getType().equals(finMchAccountLs.getTransType())) {
                // 退款
                payType[1] += finMchAccountLs.getTransAmount();
                map.put(accountType, payType);
                continue;
            }
            log.warn("本条商户流水消费类型不正确{}", finMchAccountLs);
        }
        return map;
    }

    /**
     * 计算用户表相关金额
     *
     * @param finUserAccountLsList 要计算的流水
     * @return
     */
    private Map<String, long[]> calUserMoney(List<FinUserAccountLs> finUserAccountLsList) {
        Map<String, long[]> map = new HashMap<>();
        for (FinUserAccountLs finUserAccountLs : finUserAccountLsList) {
            String accountType = finUserAccountLs.getAccountType();
            long[] payType = map.get(accountType);
            if (payType == null) {
                payType = new long[]{0, 0};
            }
            if (TransTypeEnum.CONSUME_TYPE.getType().equals(finUserAccountLs.getTransType())) {
                // 消费
                payType[0] += finUserAccountLs.getTransAmount();
                map.put(accountType, payType);
                continue;
            }
            if (TransTypeEnum.REFOUND_TYPE.getType().equals(finUserAccountLs.getTransType())) {
                // 退款
                payType[1] += finUserAccountLs.getTransAmount();
                map.put(accountType, payType);
                continue;
            }
            log.warn("本条用户流水消费类型不正确{}", finUserAccountLs);
        }
        return map;
    }

    // 查询在本次盘账结束时间前入住的启用所有商户
    public List<FinMchAccountBalance> queryMchAccountList() {
        return finMchAccountBalanceDao.queryAllMchBeforeTime();
    }

    /**
     * 审核 商户结算账单
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResp reviewMchSettleBill(ReviewReq reviewReq) {
        BaseResp resp = new BaseResp();
        resp.setCodeAndMsg(ErrorCodeEnum.FAIL);
        if (MchSettleBillStatusEnum.FINANCE_PASS.getStatus().equals(reviewReq.getReviewResult())) {
            // 财务人员审核通过,同时更新账单表,商户流水表,用户流水表
            List<FinMchSettleBill> finMchSettleBillList = finMchSettleBillDao.queryByBatchNo(reviewReq.getBatchNo());
            if (finMchSettleBillList == null || finMchSettleBillList.isEmpty()) {
                resp.setMsg("这个批次号不存在");
                return resp;
            }
            try {
                updateMchSettleBillAndLs(finMchSettleBillList.get(0), reviewReq);
                resp.setCodeAndMsg(ErrorCodeEnum.SUCCESS);
                return resp;
            } catch (Exception e) {
                resp.setMsg(String.format("更新商户账单失败,失败信息%s", e.getMessage()));
                e.printStackTrace();
                return resp;
            }
        } else if (MchSettleBillStatusEnum.FINANCE_REFUSE.getStatus().equals(reviewReq.getReviewResult())) {
            // 财务人员审核不通过,只需要更新账单批次表
            try {
                updateMchSettleBill(reviewReq);
                resp.setCodeAndMsg(ErrorCodeEnum.SUCCESS);
                return resp;
            } catch (Exception e) {
                log.error("更新商户账单失败,失败信息{}", e.getMessage(),e);
                //resp.setMsg(String.format("更新商户账单失败,失败信息%s", e.getMessage()),e);
            }
        }
        resp.setMsg("你的审核类型不对啊");
        return resp;
    }

    /**
     * 同时更新,流水状态,账单状态
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMchSettleBillAndLs(FinMchSettleBill finMchSettleBill, ReviewReq reviewReq) throws Exception {
        updateLsStatus(finMchSettleBill.getIncomeCount(), finMchSettleBill.getAccountNo(), finMchSettleBill.getBeginDate()
                , finMchSettleBill.getEndDate(), TransStatusEnum.CLSD.getStatus(), TransStatusEnum.CLSDING.getStatus());
        updateMchSettleBill(reviewReq);
        updateMchBalance(finMchSettleBill, reviewReq);
    }

    /**
     * 更新商户余额
     */
    private void updateMchBalance(FinMchSettleBill finMchSettleBill, ReviewReq reviewReq) throws Exception {
        long money = finMchSettleBill.getIncomeAmount() - finMchSettleBill.getExpandAmount();
        int update = finMchAccountBalanceDao.updateSettleBalance(money, finMchSettleBill.getAccountNo(), reviewReq.getUpdateOperator());
        if (update <= 0) {
            throw new Exception(String.format("更新%d条商户余额,账单数据是%s,请求数据%s", update, finMchSettleBill, reviewReq));
        }
    }

    /**
     * 更新账单表
     */
    private void updateMchSettleBill(ReviewReq reviewReq) throws Exception {
        int update = finMchSettleBillDao.updateByBatchNo(reviewReq.getBatchNo(), reviewReq.getReviewResult(), reviewReq.getUpdateOperator());
        if (update <= 0) {
            throw new Exception(String.format("更新%d条账单,数据是%s", update, reviewReq));
        }
    }
}
