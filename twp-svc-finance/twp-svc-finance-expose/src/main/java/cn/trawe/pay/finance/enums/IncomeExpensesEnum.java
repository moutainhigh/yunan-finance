
package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  AccountStatusEnum   
 * @Description:用户账户状态 0：以删除；1：启用；2：停用
 * @author: jianjun.chai 
 * @date:   2019年11月14日 上午10:38:21   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum IncomeExpensesEnum {
	
	INCOME("IN","收入"),
	EXPENSES("OUT","支出")
    ;

	
	private String type;
	
	
    
	
  


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
















	private String desc;

    private IncomeExpensesEnum(String type,String desc) {
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
    	IncomeExpensesEnum[] ary = IncomeExpensesEnum.values();
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
    	IncomeExpensesEnum[] ary = IncomeExpensesEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static IncomeExpensesEnum getEnum(String name) {
    	IncomeExpensesEnum[] arry = IncomeExpensesEnum.values();
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
    	IncomeExpensesEnum[] enums = IncomeExpensesEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (IncomeExpensesEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

}
