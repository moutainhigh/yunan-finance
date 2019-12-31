
package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  TransStatusEnum   
 * @Description:交易状态枚举
 *   交易状态（0 :已结算 1：正常 2：提现成功 3：提现失败 4：存疑（后续账务比对会使用，存疑的流水不参与结算 5：已冲正 6: 已退款 7：结算中）
 * @author: jianjun.chai 
 * @date:   2019年11月13日 下午4:47:14   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum TransStatusEnum {
	
    CLSD(0,"已结算"),
    NORMAL(1,"正常"),
    WITHDRAWAL_SUCCESS(2,"提现成功"),
    WITHDRAWAL_ERROR(3,"提现失败"),
    DOUBT(4,"存疑"),
    REVERSAL(5,"已冲正"),
    REFUND(6,"已退款"),
    CLSDING(7,"结算中")
    ;

	
	private int status;
	
	
    
	
    /**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	private String desc;

    private TransStatusEnum(int status,String desc) {
        this.desc = desc;
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static Map<String, Map<String, Object>> toMap() {
    	TransStatusEnum[] ary = TransStatusEnum.values();
        Map<String, Map<String, Object>> enumMap = new HashMap<String, Map<String, Object>>();
        for (int num = 0; num < ary.length; num++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String key = ary[num].name();
            map.put("desc", ary[num].getDesc());
            enumMap.put(key, map);
        }
        return enumMap;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List toList() {
    	TransStatusEnum[] ary = TransStatusEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static TransStatusEnum getEnum(String name) {
    	TransStatusEnum[] arry = TransStatusEnum.values();
        for (int i = 0; i < arry.length; i++) {
            if (arry[i].name().equalsIgnoreCase(name)) {
                return arry[i];
            }
        }
        return null;
    }
















    /**
     * 取枚举的json字符串
     *
     * @return
     */
    public static String getJsonStr() {
    	TransStatusEnum[] enums = TransStatusEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (TransStatusEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

}
