/**
 * All rights Reserved, Designed By www.trawe.cn
 *
 * @Title: AccountTypeEnum.java
 * @Package cn.trawe.pay.finace.common.enums
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: jianjun.chai
 * @date: 2019年11月12日 上午10:24:57
 * @version V1.0
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AccountTypeEnum
 * @Description:账户类型枚举
 * @author: jianjun.chai
 * @date: 2019年11月12日 上午10:24:57
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum AccountTypeEnum {

    ALL_ACCOUNT("ALL", "汇总"),

    PLAT_ACCOUNT("PLA", "虚拟账户"),

    WX_ACCOUNT("WX", "微信虚拟账户"),

    ALI_ACCOUNT("ALI", "支付宝虚拟账户"),

    ETC_ACCOUNT("ETC", "ETC虚拟账户");


    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    private String desc;

    private AccountTypeEnum(String account, String desc) {
        this.desc = desc;
        this.account = account;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Map<String, Map<String, Object>> toMap() {
        AccountTypeEnum[] ary = AccountTypeEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = ary[num].name();
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List toList() {
        AccountTypeEnum[] ary = AccountTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static AccountTypeEnum getEnum(String name) {
        AccountTypeEnum[] arry = AccountTypeEnum.values();
        for (int i = 0; i < arry.length; i++) {
            if (arry[i].name().equalsIgnoreCase(name)) {
                return arry[i];
            }
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getWayList(String account) {
        AccountTypeEnum[] ary = AccountTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            if (ary[i].account.equals(account)) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("desc", ary[i].getDesc());
                map.put("name", ary[i].name());
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 取枚举的json字符串
     *
     * @return
     */
    public static String getJsonStr() {
        AccountTypeEnum[] enums = AccountTypeEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (AccountTypeEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

    public static void main(String[] args) {
        String accountType = AccountTypeEnum.PLAT_ACCOUNT.account;

    }

}
