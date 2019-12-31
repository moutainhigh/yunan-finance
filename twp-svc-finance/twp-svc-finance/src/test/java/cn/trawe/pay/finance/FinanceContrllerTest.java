/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  FinanceContrllerTest.java   
 * @Package cn.trawe.pay.finance   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月27日 上午10:32:00   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ctc.wstx.util.DataUtil;
import com.ibm.icu.math.BigDecimal;

import cn.trawe.pay.finance.account.controller.FinanceApiController;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;
import cn.trawe.pay.finance.dto.req.AccountRechargeReq;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
import cn.trawe.pay.finance.dto.req.AccountWithDrawReq;
import cn.trawe.pay.finance.dto.req.TradeUpdateReq;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.useraccount.service.impl.UserRefundServiceImpl;
import cn.trawe.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  FinanceContrllerTest   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: jianjun.chai 
 * @date:   2019年11月27日 上午10:32:00   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Slf4j
public class FinanceContrllerTest  extends BaseTest {
	
	
	   @Autowired
	   private FinanceApiController  FinanceApiController;
	   
	   @Autowired
	   UserRefundServiceImpl   UserRefundServiceImpl;
	   
	   //用户账户查询
	   
	   @Test
	   public void queryUserAccount() {
		   FinanceApiController.getUserAccountByUserId("USER20191129172155674614");
	   }
	
	   //用户余额消费 
	   @Test
	   public void consumeBalance() {
		   
		   AccountConsumeReq  req = new AccountConsumeReq();
		   req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		   req.setUserId("USER20191127113831139423");
		   req.setMchId("YTK_MCH_ID1574159160693");
		   req.setAmount(1000L);
		   req.setMerServiceAmount(10L);
		   req.setMerServiceRate("0.01");
		   req.setOutOrderNo("O1234567893");
		   req.setOutOrderType("1");
		   req.setTransDate(new Date());
		   FinanceApiController.consumeAccount(req);
		   
	   }
	   
	   //用户微信消费 
	   @Test
	   public void consumeWX() {
		   
		   AccountConsumeReq  req = new AccountConsumeReq();
		   req.setAccountType(AccountTypeEnum.WX_ACCOUNT.getAccount());
		   req.setUserId("USER20201127113831139423");
		   req.setMchId("YTK_MCH_ID1574159160693");
		   req.setAmount(1000L);
		   req.setMerServiceAmount(10L);
		   req.setMerServiceRate("0.01");
		   req.setOutOrderNo("O1234567891");
		   req.setOutOrderType("1");
		   req.setTransDate(new Date());
		   FinanceApiController.consumeAccount(req);
		   
	   }
	   
	   
	   //用户充值
	   @Test
	   public void rechargeUserBalance() {
		   AccountRechargeReq  req = new AccountRechargeReq();
		   req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		   req.setUserId("USER20191127113831139423");
		   req.setOutOrderNo("O1234567894");
		   req.setOutOrderType("1");
		   req.setTransDate(new Date());
		   req.setMerServiceAmount(0L);
		   req.setMerServiceRate("0");
		   req.setAmount(1000L);
		   FinanceApiController.rechargeAccount(req);
	   }
	   
	   
	   //商户充值
	   @Test
	   public void rechargeMchBalance() {
		   AccountRechargeReq  req = new AccountRechargeReq();
		   req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		   req.setUserId("TW_MCH_ID1574159160693");
		   req.setOutOrderNo("O1234567895");
		   req.setOutOrderType("1");
		   req.setTransDate(new Date());
		   req.setMerServiceAmount(0L);
		   req.setMerServiceRate("0");
		   req.setAmount(1000L);
		   FinanceApiController.rechargeAccount(req);
	   }
	   
	   //商户提现
	   @Test
	   public void withDrawMch() {
		   AccountWithDrawReq req = new AccountWithDrawReq();
		   req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		   req.setAmount(1000L);
		   req.setMchId("TW_MCH_ID15748377827228881");
		   req.setMerServiceAmount(0L);
		   req.setMerServiceRate("0");
		   req.setOutOrderNo("O1234567896");
		   req.setOutOrderType("1");
		   req.setTransDate(new Date());
		   FinanceApiController.withdrawAccount(req);
	   }
	   
	   @Test
	   public void updateTrade() {
		   TradeUpdateReq req = new TradeUpdateReq();
		   req.setAmount(1000L);
		   FinanceApiController.updateTradeNo(req);
	   }
	   
	   protected   static final BigDecimal plateRate =new BigDecimal("0.006");
	   //退款
	   @Test
	   public void refund() {
		   AccountRefundReq  req  = new AccountRefundReq();
		   req.setMchId("YTK_MCH_ID1574159160692");
		   req.setAmount(90000L);
		   req.setOutOrderNo("PAY201911291544475708850548");
		   req.setOutOrderType("CONSUMPTION_000001");
		   req.setTransDate(DateUtils.parse("2019-11-29 15:44:47"));
		   req.setOutRefundOrderNo("PAY202011291026355638181113");
		   req.setOutRefundOrderType("REFUND_000001");
		   req.setMerServiceAmount(0L);
		   req.setMerServiceRate("0");
		   req.setUserId("USER20191127184322281633");
		   req.setAccountType(AccountTypeEnum.PLAT_ACCOUNT);
		   req.setPlatServiceAmount(calculatePlateRateAmount(req.getAmount()));
	       req.setPlatServiceRate(plateRate.toString());
		   req.setOutRefundOrderDate(new Date());

		   System.out.println(JSON.toJSONString(req));
//		   UserRefundServiceImpl.userRefund(req);
	   }
	   
	   public Long calculatePlateRateAmount(Long amount) {
			
			
			BigDecimal amountBig = new BigDecimal(amount);
	    	BigDecimal multiply = plateRate.multiply(amountBig);
	    	log.info("平台支出手续费金额:"+multiply.doubleValue());
	        //四舍五入
	        Long result = multiply.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
	        log.info("平台支出手续费金额四舍五入后:"+result);
	        return result;
		}

}
