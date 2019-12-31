package cn.trawe.pay.finance.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;
import cn.trawe.pay.finance.dto.req.AccountRechargeReq;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
import cn.trawe.pay.finance.dto.req.AccountWithDrawReq;
import cn.trawe.pay.finance.dto.req.MchAccountReq;
import cn.trawe.pay.finance.dto.req.TradeUpdateReq;
import cn.trawe.pay.finance.dto.req.UserAccountReq;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;

/**
 * 
 * @ClassName:  FinanceApi   
 * @Description:清分结算API  
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午10:18:51   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@RequestMapping("/finance")
public interface FinanceApi {

  

    @GetMapping(path = "/testGetUrl")
    public BaseResp testGetUrl(String msg);
    
    
    //查询用户虚拟账户信息（根据用户ID）
    @PostMapping(path = "/account/user/query")
    public BaseResp<FinUserAccountBalance> getUserAccountByUserId(@RequestParam("userId") String userId);
    
    //商户账户查询
    @PostMapping(path = "/account/mer/query")
    public BaseResp<FinMchAccountBalance> getMerAccountByMerId(@RequestParam("merId")  String merId);
    
    
    
    
    //商户开总账户及虚拟账户（根据商户ID）
    @PostMapping(path = "/account/mch/create")
    public  BaseResp  createMerAccountAndSubAccount(@RequestBody MchAccountReq  req);
    
    //用户开立总账户及虚拟账户（只有一个子账户（虚拟账户））
    @PostMapping(path = "/account/user/create")
    public  BaseResp  createUserAccountAndSubAccount(@RequestBody UserAccountReq  req);
    
    
    //消费
    @PostMapping(path = "/account/consume")
    public BaseResp  consumeAccount(@RequestBody AccountConsumeReq req);
    
    
    //提现
    @PostMapping(path = "/account/withdraw")
    public BaseResp  withdrawAccount(@RequestBody AccountWithDrawReq req);
    
    //更新流水状态接口
    @PostMapping(path = "/account/trade_no/update")
    public BaseResp updateTradeNo(@RequestBody TradeUpdateReq req);
    
    //充值
    @PostMapping(path = "/account/recharge")
    public BaseResp rechargeAccount(@RequestBody AccountRechargeReq req );
    
    //退款
    @PostMapping(path = "/account/refund")
    public BaseResp refundAccount(@RequestBody AccountRefundReq req);
    
    
    

}