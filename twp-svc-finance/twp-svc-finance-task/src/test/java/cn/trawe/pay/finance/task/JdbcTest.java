package cn.trawe.pay.finance.task;

import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountSettleBatchDao;
import cn.trawe.pay.finance.meraccount.dao.FinMchAccountLsDao;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountSettleBatchTb;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.meraccount.entity.FinMchSettleBill;
import cn.trawe.pay.finance.task.service.SettleService;
import cn.trawe.pay.finance.task.utils.DateUtil;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountLsDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liguangyan
 * @date 2019/11/28 11:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTest {
    @Autowired
    private FinMchAccountSettleBatchDao finMchAccountSettleBatchDao;

    @Autowired
    private FinMchAccountLsDao finMchAccountLsDao;

    @Autowired
    private FinUserAccountLsDao finUserAccountLsDao;

    @Autowired
    private SettleService settleService;

    @Test
    public void insert() {
        FinMchAccountSettleBatchTb finMchAccountSettleBatchTb = new FinMchAccountSettleBatchTb();
        finMchAccountSettleBatchTb.setAccountNo("123");
        finMchAccountSettleBatchTb.setCreateTime(new Date());
        finMchAccountSettleBatchTb.setUpdateTime(new Date());
        finMchAccountSettleBatchTb.setStatus(12);
        finMchAccountSettleBatchTb.setBatchVersion(new Date());
        finMchAccountSettleBatchDao.save(finMchAccountSettleBatchTb);
    }

    @Test
    public void testReview() {


    }

    @Test
    public void test() {
        //settleService.settle();
    }

    @Test
    public void select() {
        Date date = new Date();
        Date startTime = DateUtil.getZeroTime(date, -1);
        Date endTime = DateUtil.getZeroTime(date, 0);

        List<Map<String, Object>> mchList = finMchAccountLsDao.queryCountAndTotalTransAmount("yan_test", startTime, endTime);
        System.out.println("查询到的数据是" + mchList);

    }

    @Test
    public void selectCount() {
        Date date = new Date();
        Date startTime = DateUtil.getZeroTime(date, -10);
        Date endTime = DateUtil.getZeroTime(date, 2);
        List<Map<String, Object>> userMaps = finUserAccountLsDao.queryCountAndTotalTransAmount("yan_123", startTime, endTime);
        List<Map<String, Object>> mchMaps = finMchAccountLsDao.queryCountAndTotalTransAmount("yan_123", startTime, endTime);
        StringBuilder stringBuilder = new StringBuilder();

        for (Map<String, Object> map : mchMaps) {
            String accountType = (String) map.get("accountType");
            System.out.println(accountType);
        }
        System.out.println("商户查询结果是" + mchMaps);
        System.out.println("用户查询结果是" + userMaps);
    }

    @Test
    public void testUpdate() {
//       String accountNo, int newTransStatus, int oldTransStatus, Date beginTime, Date endTime
        Date date = new Date();
        Date startTime = DateUtil.getZeroTime(date, -10);
        Date endTime = DateUtil.getZeroTime(date, 2);
        int num = finMchAccountLsDao.updateLsStatus("yan_test", TransStatusEnum.CLSDING.getStatus(), TransStatusEnum.NORMAL.getStatus(), startTime, endTime);
        int num2 = finUserAccountLsDao.updateLsStatus("yan_test", TransStatusEnum.CLSDING.getStatus(), TransStatusEnum.NORMAL.getStatus(), startTime, endTime);

        System.out.println("商户更新数量是" + num);
        System.out.println("用户更新数量是" + num2);


    }


}
