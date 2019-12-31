/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  VirtualAccountStatusEnum.java   
 * @Package cn.trawe.pay.finance.enums   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年12月10日 上午11:02:56   
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
 * @ClassName:  VirtualAccountStatusEnum   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: jianjun.chai 
 * @date:   2019年12月10日 上午11:02:56   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public enum VirtualAccountStatusEnum {
	
	
	OPEN(1,"已开通"),
    NOT_OPEN(0,"未开通")
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

    private VirtualAccountStatusEnum(int status,String desc) {
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
    	VirtualAccountStatusEnum[] ary = VirtualAccountStatusEnum.values();
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
    	VirtualAccountStatusEnum[] ary = VirtualAccountStatusEnum.values();
        List list = new ArrayList();
        for (int i = 0; i < ary.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("desc", ary[i].getDesc());
            map.put("name", ary[i].name());
            list.add(map);
        }
        return list;
    }

    public static VirtualAccountStatusEnum getEnum(String name) {
    	VirtualAccountStatusEnum[] arry = VirtualAccountStatusEnum.values();
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
    	AccountStatusEnum[] enums = AccountStatusEnum.values();
        StringBuffer jsonStr = new StringBuffer("[");
        for (AccountStatusEnum senum : enums) {
            if (!"[".equals(jsonStr.toString())) {
                jsonStr.append(",");
            }
            jsonStr.append("{id:'").append(senum).append("',desc:'").append(senum.getDesc()).append("'}");
        }
        jsonStr.append("]");
        return jsonStr.toString();
    }

}
