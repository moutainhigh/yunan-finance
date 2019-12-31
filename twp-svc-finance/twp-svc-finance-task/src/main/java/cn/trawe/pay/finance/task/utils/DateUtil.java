package cn.trawe.pay.finance.task.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 *
 * @author liguangyan
 * @date 2019/11/28 15:02
 */
public class DateUtil {
    /**
     * 获取某一天零点时间
     *
     * @param date      日期
     * @param apartDays 日期偏移量
     * @return
     */
    public static Date getZeroTime(Date date, int apartDays) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        // 日期偏移
        calendar.add(Calendar.DATE, apartDays);
        return calendar.getTime();
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String DateToStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }


}
