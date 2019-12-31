package cn.trawe.pay.finance.useraccount.service;

import java.util.Date;
import java.util.List;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserSubAccountBalance;

/**
 * 
 * @ClassName:  TwFinAccountTransactionService   
 * @Description:云南项目账户业务层（事务控制）
 * @author: jianjun.chai 
 * @date:   2019年11月11日 上午11:31:02   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public interface TwFinUserAccountTransactionService {
	
	
	//查询用户虚拟总账户信息（根据用户ID）isForUpdate false 不加锁 true 加锁 
	public FinUserAccountBalance getByUserId(String userId,String accountType,boolean isForUpdate );
	
	public BaseResp getDataByUserId(String userId,String accountType,boolean isForUpdate );

	//查询用户第三方支付虚拟账户列表(根据用户总账户编号)
	//public List<FinUserSubAccountBalance> getDataListByAccountNo(String userAccountNo);
	
	//用户开立总账户及虚拟账户（只有一个子账户（虚拟账户））
	public  BaseResp  createUserAccount(String userId,String userName,String accountType);
	
	//用户消费
	/**
	 * 
	 * @Title: debitAccount   
	 * @Description: 用户消费  
	 * @param userId   用户ID
	 * @param merId    商户ID
	 * @param accountType    账户类型
	 * @param amount         交易金额
	 * @param serviceAmount  手续费金额
	 * @param serviceRate  手续费费率
	 * @param outOrderNo   订单号
	 * @param outOrderType 订单类型
	 * @param trans_no  交易流水号
	 * @return      
	 * FinUserAccountBalance  用户账户实体    
	 * @throws
	 */
	public BaseResp  consumeAccount(String userId,String merId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl);
	
	
	//用户提现
    /**
     * 
     * @Title: withdrawAccount   
     * @Description: 用户账户提现   
     * @param userId
     * @param amount
     * @param bankNo
     * @param outOrderNo
     * @param outOrderType
     * @return      
     * FinUserAccountBalance      
     * @throws
     */
	public FinUserAccountBalance withdrawAccount(String userId,Long amount,String outOrderNo,String outOrderType);
	
	
	/**
	 * 
	 * @Title: rechargeAccount   
	 * @Description: 用户充值
	 * @param userId  用户ID
	 * @param accountType  账户类型
	 * @param amount       交易金额
	 * @param serviceAmount 手续费
	 * @param serviceRate  费率
	 * @param outOrderNo   订单号
	 * @param outOrderType 订单类型
	 * @param trans_no  交易流水号
	 * @param notifyUrl  通知地址
	 * @return      
	 * FinUserAccountBalance      
	 * @throws
	 */
	public  BaseResp  rechargeAccount(String userId,String accountType,Long amount,Long merServiceAmount,String merServiceRate,Long platServiceAmount,String platServiceRate,String outOrderNo,String outOrderType,Date transDate,String notifyUrl);
	
	
	
	
	
	
	
	
	
	
	
	
	

}
