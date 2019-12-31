package cn.trawe.pay.finance.task.test;

import cn.trawe.pay.finance.meraccount.entity.FinMchSettleBill;
import cn.trawe.pay.finance.task.utils.DateUtil;
import com.sun.javafx.binding.StringFormatter;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liguangyan
 * @date 2019/11/28 11:40
 */
public class MainTest {
    public static void main(String[] args) {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Date zeroTime = DateUtil.getZeroTime(new Date(), -1);
//        String format = df.format(zeroTime);
//        System.out.println(format);

//        Map<String, String> map = new HashMap<>();
//        map.put("aa", "123");
//        map.put("bb", "455");
//        String format = String.format("键都是%d哈哈", map.keySet());
//        System.out.println(format);

        FinMchSettleBill finMchSettleBillAll = new FinMchSettleBill();
        finMchSettleBillAll.setIncomeCount((finMchSettleBillAll.getIncomeCount() == null ? 0 : finMchSettleBillAll.getIncomeCount()) + 12);
        finMchSettleBillAll.setIncomeCount((finMchSettleBillAll.getIncomeCount() == null ? 0 : finMchSettleBillAll.getIncomeCount()) + 12);
        System.out.println(finMchSettleBillAll.getIncomeCount());

    }
}
