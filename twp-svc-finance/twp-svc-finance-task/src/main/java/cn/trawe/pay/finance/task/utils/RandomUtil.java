package cn.trawe.pay.finance.task.utils;

import java.util.UUID;

/**
 * 随机数工具类
 *
 * @author liguangyan
 * @date 2019/12/5 15:35
 */
public class RandomUtil {

    /**
     * 创建时间戳 + uuid的随机字符串
     *
     * @return
     */
    public static String createRamdomWithTimeAndUUID() {
        return System.currentTimeMillis() + getUUID();
    }

    /**
     * 32位uuid
     *
     * @return
     */
    private static String getUUID() {
        String uuid = UUID.randomUUID().toString();    //转化为String对象
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
