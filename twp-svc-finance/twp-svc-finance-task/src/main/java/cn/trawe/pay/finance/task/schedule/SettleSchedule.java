package cn.trawe.pay.finance.task.schedule;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.common.client.RedisClient;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.task.service.BussinessSettleService;
import cn.trawe.pay.finance.task.service.SettleService;
import cn.trawe.pay.finance.task.utils.DateUtil;
import cn.trawe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 结算盘账
 *
 * @author liguangyan
 * @date 2019/11/27 19:05
 */
@EnableScheduling
@Component
@Slf4j
public class SettleSchedule {
    @Autowired
    private SettleService settleService;
    
    
    @Autowired 
    BussinessSettleService  bussinessSettleService;
    
    protected  int TIMEOUT_LOCK = 60;
    /**
     * 获取锁的次数
     */
    protected  int TRY_LOCK_COUNT = 3;
    
    
    @Autowired
    private RedisClient redisClient;
    // 清算日期,是今天之前的第几天
    @Value("${finance.task.settleApartDays}")
    private Integer settleApartDays;
    
   

    // 盘账
    @Scheduled(cron = "${finance.task.schedule.settle}")
    public void settle() {
    	
    	Date date = new Date();
        Date beginTime = DateUtil.getZeroTime(date, settleApartDays);
        Date endTime = DateUtil.getZeroTime(date, settleApartDays + 1);
        // 查询所有商户信息
        List<FinMchAccountBalance> finMchAccountBalances = settleService.queryMchAccountList();
        //System.out.println("查询到的商户信息是" + finMchAccountBalances);
        if (finMchAccountBalances == null || finMchAccountBalances.isEmpty()) {
            log.warn("盘账的时候,没有查询到商户信息");
            return;
        }
        for (FinMchAccountBalance finMchAccountBalance : finMchAccountBalances) {
            // 给每个商户盘账
            if (StringUtils.isBlank(finMchAccountBalance.getAccountNo())) {
                log.error("要盘账的商户号为空{}",JSON.toJSONString(finMchAccountBalance));
                continue;
            }
            
            
            //一个商户开启一个事务
            boolean isGetLock = false;
            String lockKey = finMchAccountBalance.getAccountNo();
            try {
                try {
                    isGetLock = redisClient.tryLock(lockKey, TIMEOUT_LOCK, TRY_LOCK_COUNT);
                } catch (Throwable ex) {
                    LogUtil.error(log,  finMchAccountBalance.getAccountNo(), "获取锁出错异常：" + ex.getMessage());
                    continue;
                }
                if (!isGetLock) {
                    LogUtil.error(log,  finMchAccountBalance.getAccountNo(), "该商户正在清分处理中" );
                    continue;
                }
            	bussinessSettleService.settle(finMchAccountBalance.getAccountNo(), beginTime, endTime);
			} catch (Exception e) {
				log.error(e.getLocalizedMessage(),e.fillInStackTrace());
			}
            finally {
            if (isGetLock) {
                redisClient.unlock(lockKey);
            }
          }
        }
    }

}
