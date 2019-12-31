/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  CreateTradeNoService.java   
 * @Package cn.trawe.pay.finance.core   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月13日 上午11:16:19   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.core;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.trawe.utils.DateUtils;

/**   
 * @ClassName:  CreateTradeNoService   
 * @Description:交易流水号生成类 
 * @author: jianjun.chai 
 * @date:   2019年11月13日 上午11:16:19   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@Service
public class CreateTradeNoService {
	
	/**
	 * 
	 * @Title: createTradeNo   
	 * @Description: 交易流水号生成类 (YYYYMMDDHHMMSS+随机数length)
	 * @param tradeTime   交易日期  
	 * @param type    交易类型
	 * @param length  随机数长度
	 * @return      
	 * String      
	 * @throws
	 */
	public String createTradeNo(Date tradeTime,String type,int length){
		
		String tradeNo = "";
		if(StringUtils.isNotBlank(type)) {
			tradeNo=type;
		}
		tradeNo = tradeNo+DateUtils.format(tradeTime, DateUtils.YYYYMMDDHHMMSS) + RandomStringUtils.randomNumeric(length);	
		return tradeNo;
		
	}
	public static void main(String[] args) {
		CreateTradeNoService  service = new CreateTradeNoService();
		System.out.println(service.createTradeNo(new Date(),"",10));
	}

}
