package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 客户类型
 * @author jianjun.chai
 *
 */
public enum CustomerTypeEnum {
	
	USER("USER","用户"),
	MCH("MCH","商户")
    ;

	
	private String type;
	
	
    
	
  
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	private String desc;

    private CustomerTypeEnum(String type,String desc) {
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
    	CustomerTypeEnum[] ary = CustomerTypeEnum.values();
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
    	CustomerTypeEnum[] ary = CustomerTypeEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static CustomerTypeEnum getEnum(String name) {
    	CustomerTypeEnum[] arry = CustomerTypeEnum.values();
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
