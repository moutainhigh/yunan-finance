/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  TwFinMerAccountTransactionService.java   
 * @Package cn.trawe.pay.finance.service   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月11日 下午2:06:59   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.meraccount.service;

import java.util.Date;
import java.util.List;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.meraccount.entity.FinMchSubAccountBalance;

/**   
 * @ClassName:  TwFinMerAccountTransactionService   
 * @Description:云南项目商户账户控制层
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午2:06:59   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public interface TwFinMerAccountTransactionService {
	
	
	
	

	    //通过商户查询商户总账户信息
		public FinMchAccountBalance getDataByMerId(String merId,String accountType);
		
		
		//通过商户查询商户子账户信息根据账户类型
		//public FinMchSubAccountBalance getSubDataByMchAccountNoAndAccountType(String mchAccountNo,String accountType);
		

		//查询商户子账户列表
		//public List<FinMchSubAccountBalance> getSubDataListByMchAccountNo(String mchAccountNo);
		
		//商户开总账户及虚拟账户（根据商户ID）
		public  BaseResp  createMerAccount(String mchId, String mchName,
				   String settlementCycle,String accountType);
		
		//开立商户ETC账户（根据商户ID）
		public  BaseResp  createMerSubETCAccount(String mchId,String mchName);
		
		//商户提现
		public BaseResp  withdrawAccount(String mchId,Long amount,String outOrderNo,String outOrderType,Date transDate,Long platServiceAmount,String platServiceRate,Long merServiceAmount,String MerServiceRate);
		
		//商户充值
		public BaseResp  rechargeAccount(String merId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl);
	

		 //商户退款
		public BaseResp  refundAccount(String userId,String merId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl);
}
