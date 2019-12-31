package cn.trawe.pay.finance.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.account.notify.produce.RocketMqSender;
import cn.trawe.pay.finance.account.service.FinanceApiService;
import cn.trawe.pay.finance.api.FinanceApi;
import cn.trawe.pay.finance.core.BaseServiceImpl;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;
import cn.trawe.pay.finance.dto.req.AccountRechargeReq;
import cn.trawe.pay.finance.dto.req.AccountRefundReq;
import cn.trawe.pay.finance.dto.req.AccountWithDrawReq;
import cn.trawe.pay.finance.dto.req.MchAccountReq;
import cn.trawe.pay.finance.dto.req.TradeUpdateReq;
import cn.trawe.pay.finance.dto.req.UserAccountReq;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.enums.CustomerTypeEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.pay.finance.mq.req.MqFinanceBaseReq;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.service.impl.UserRefundServiceImpl;
import cn.trawe.util.L;
import cn.trawe.util.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "FinanceApiController|API服务控制器")
@Slf4j
public class FinanceApiController extends BaseServiceImpl implements FinanceApi {

    @Autowired
    private FinanceApiService financeApiService;

    @Autowired
	UserRefundServiceImpl userRefundServiceImpl;


    @ApiOperation(value = "清分结算服务测试接口️")
    @Override
    public BaseResp testGetUrl(String msg) {
        BaseResp   resp  = BaseResp.successInstance();
        log.info(L.b("test").msg(resp.toString()).s());
        MqFinanceBaseReq<AccountConsumeReq> consumeReq = new MqFinanceBaseReq<AccountConsumeReq>();
        AccountConsumeReq bodReq = new AccountConsumeReq();
        bodReq.setUserId("USER_ID --> CHAI");
        consumeReq.setData(bodReq);
        consumeReq.setBusType(TransTypeEnum.CONSUME_TYPE);
        RocketMqSender.sendStringMsg(consumeReq.toString());
        return resp;
    }




	@Override
	public BaseResp<FinUserAccountBalance> getUserAccountByUserId(@RequestParam("userId")  String userId) {
		log.info(L.b(userId).bizType("[用户账户查询请求]").m("用户ID : " + userId).s());
		BaseResp successInstance = userService.getDataByUserId(userId, AccountTypeEnum.PLAT_ACCOUNT.getAccount(),false);
		log.info(L.b(userId).bizType("[用户账户查询响应]").m(JSON.toJSONString(successInstance)).s());
		return successInstance;
	}




	@Override
	public BaseResp createMerAccountAndSubAccount(@RequestBody @Validated MchAccountReq req) {
		 log.info(L.b(req.getMchId()).bizType("[商户账户创建请求]").m(JSON.toJSONString(req)).s());
		 BaseResp createMerAccountAndSubAccount = merService.createMerAccount(req.getMchId(), req.getMchName(),req.getSettlementCycle(), AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		 log.info(L.b(req.getMchId()).bizType("[商户账户创建响应]").m(JSON.toJSONString(createMerAccountAndSubAccount)).s());
		 return createMerAccountAndSubAccount;
	}




	@Override
	public BaseResp createUserAccountAndSubAccount(@RequestBody @Validated UserAccountReq req) {
		   log.info(L.b(req.getUserId()).bizType("[用户账户创建请求]").m(JSON.toJSONString(req)).s());  
		   BaseResp createUserAccountAndSubAccount = userService.createUserAccount(req.getUserId(),req.getUserName(),AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		   log.info(L.b(req.getUserId()).bizType("[用户账户创建响应]").m(JSON.toJSONString(createUserAccountAndSubAccount)).s());
		   return createUserAccountAndSubAccount;
	}




	@Override
	public BaseResp consumeAccount(@RequestBody  @Validated AccountConsumeReq req) {
		     
		 log.info(L.b(req.getOutOrderNo()).bizType("[消费请求]").m(JSON.toJSONString(req)).s());  
		 BaseResp successInstance = financeApiService.consume(req);
		 log.info(L.b(req.getOutOrderNo()).bizType("[消费响应]").m(JSON.toJSONString(successInstance)).s());
		 return successInstance;
	}




	@Override
	public BaseResp withdrawAccount(@RequestBody @Validated AccountWithDrawReq req) {
		 log.info(L.b(req.getOutOrderNo()).bizType("[提现请求]").m(JSON.toJSONString(req)).s());  
		 BaseResp successInstance = financeApiService.withDraw(req);
		 log.info(L.b(req.getOutOrderNo()).bizType("[提现响应]").m(JSON.toJSONString(successInstance)).s());
		 return successInstance; 
	}




	@Override
	public BaseResp updateTradeNo(@RequestBody TradeUpdateReq req) {
		 log.info(L.b(req.getOutOrderNo()).bizType("[更新流水请求]").m("订单号: " + req.getOutOrderNo()).s());  
		 BaseResp successInstance = financeApiService.updateTradeNo(req);
		 log.info(L.b(req.getOutOrderNo()).bizType("[更新流水响应]").m(JSON.toJSONString(successInstance)).s());
		 return successInstance;
	}




	@Override
	public BaseResp rechargeAccount(@RequestBody @Validated AccountRechargeReq req) {
		log.info(L.b(req.getOutOrderNo()).bizType("[充值请求]").m(JSON.toJSONString(req)).s());  
		BaseResp successInstance = financeApiService.recharge(req);
		 log.info(L.b(req.getOutOrderNo()).bizType("[充值响应]").m(JSON.toJSONString(successInstance)).s());
		 return successInstance;
	}




	@Override
	public BaseResp refundAccount(@RequestBody  @Validated AccountRefundReq req) {
		boolean isGetLock = false;
        String lockKey = ORDER_REFUND_LOCKKEY + req.getOutOrderNo();
            try {
                try {
                    isGetLock = redisClient.tryLock(lockKey, TIMEOUT_LOCK, TRY_LOCK_COUNT);
                } catch (Throwable ex) {
                    LogUtil.error(log,  req.getOutOrderNo(), "获取锁出错异常：" + ex.getMessage());
                    return BaseResp.errorInstance(ex.getLocalizedMessage());
                  
                }
                if (!isGetLock) {
                    LogUtil.error(log,  req.getOutOrderNo(), "重复请求" );
                    return BaseResp.errorInstance("重复请求");
                }
                log.info(L.b(req.getOutOrderNo()).bizType("[退款请求]").m(JSON.toJSONString(req)).s());
       		    BaseResp successInstance = userRefundServiceImpl.userRefund(req);
       		    log.info(L.b(req.getOutOrderNo()).bizType("[退款响应]").m(JSON.toJSONString(successInstance)).s());
       		    return successInstance;
                
          }
          catch (Exception e) {
                 LogUtil.error(log, req.getOutOrderNo(), e.getMessage(), e.getCause());
                 throw e;
          } finally {
         if (isGetLock) {
             redisClient.unlock(lockKey);
         }
       }
		
	}




	@Override
	public BaseResp<FinMchAccountBalance> getMerAccountByMerId(@RequestParam("merId")  String merId) {
		log.info(L.b(merId).bizType("[商户账户查询请求]").m("商户ID : " + merId).s());
		FinMchAccountBalance merBalance = merService.getDataByMerId(merId,AccountTypeEnum.PLAT_ACCOUNT.getAccount());
		BaseResp successInstance = BaseResp.successInstance();
		successInstance.setData(merBalance);
		log.info(L.b(merId).bizType("[商户账户查询响应]").m(JSON.toJSONString(successInstance)).s());
		return successInstance;
	}
}
