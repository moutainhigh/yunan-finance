
package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  TransTypeEnum   
 * @Description:交易类型 （消费CA  充值 RA 提现 WA 退款 RF 冲正RE SA 清分结算 SC 服务费 SR 退服务费）
 * @author: jianjun.chai 
 * @date:   2019年11月13日 下午6:01:22   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum TransTypeEnum {
	
	CONSUME_TYPE("CA","消费"),
	RECHARGE_TYPE("RA","充值"),
    WITHDRAWAL_TYPE("WA","提现"),
    REFOUND_TYPE("RF","退款"),
    REVERSAL_TYPE("RE","冲正"),
    SETTLE_TYPE("SA","清分结算"),
    SERVICE_CHARGE("SC","服务费"),
    SERVICE_CHARGE_REFUND("SR","退服务费")
    ;

	
	private String type;
	
	
    
	
  
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	private String desc;

    private TransTypeEnum(String type,String desc) {
        this.desc = desc;
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public static Map<String, Map<String, Object>> toMap() {
    	TransTypeEnum[] ary = TransTypeEnum.values();
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
    	TransTypeEnum[] ary = TransTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static TransTypeEnum getEnum(String name) {
    	TransTypeEnum[] arry = TransTypeEnum.values();
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
    	TransTypeEnum[] enums = TransTypeEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (TransTypeEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

}
