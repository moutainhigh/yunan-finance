
package cn.trawe.pay.finance.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName:  MerAccountStatusEnum   
 * @Description:商户账户状态枚举
 * @author: jianjun.chai 
 * @date:   2019年11月14日 上午11:00:10   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public enum MerAccountStatusEnum {
	
	DELETED(0,"已删除"),
	OPEN(1,"启用"),
    STOP(2,"停用")
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

    private MerAccountStatusEnum(int status,String desc) {
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
    	MerAccountStatusEnum[] ary = MerAccountStatusEnum.values();
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
    	MerAccountStatusEnum[] ary = MerAccountStatusEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static MerAccountStatusEnum getEnum(String name) {
    	MerAccountStatusEnum[] arry = MerAccountStatusEnum.values();
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
    	MerAccountStatusEnum[] enums = MerAccountStatusEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (MerAccountStatusEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

}
