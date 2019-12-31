package cn.trawe.pay.finance.account.notify.listener;

import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import cn.trawe.pay.finance.FinanceConstants;
import cn.trawe.pay.finance.account.service.FinanceInterface;
import cn.trawe.pay.finance.account.service.bus.FinanceMqInterface;
import cn.trawe.pay.finance.dto.BaseResp;
import cn.trawe.pay.finance.dto.FinanceReq;
import cn.trawe.pay.finance.dto.req.AccountConsumeReq;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import cn.trawe.pay.finance.mq.req.MqFinanceBaseReq;
import cn.trawe.pay.rocketmq.MqConfigLoader;
import cn.trawe.pay.rocketmq.RocketmqManager;
import cn.trawe.util.LogUtil;



/**
 * 
 * @ClassName:  FinanceDealListener   
 * @Description:记账服务RocketMq消费者（除余额消费)
 * @author: jianjun.chai 
 * @date:   2019年12月11日 下午3:13:40   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class FinanceDealListener implements MessageListener, InitializingBean {
    private Logger logger = LoggerFactory.getLogger(FinanceDealListener.class);



    @Autowired
    MqConfigLoader configLoader;
    
    @Autowired
    private FinanceMqInterface financeMqInterface; 


    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        String body = new String(message.getBody(), Charset.forName("utf-8"));
        String tag = message.getTag();
    	String topic = message.getTopic();
        try {
            LogUtil.info(logger,message.getMsgID(),body);
            LogUtil.info(logger,message.getMsgID(), "tag :" +tag);
        	LogUtil.info(logger,message.getMsgID(), "topic :" +topic);
            if(StringUtils.isNotEmpty(body)){
            	
            	LogUtil.info(logger,message.getMsgID(), "body :" +body);
            	//统一接口多态调用之前代码
            	
            	MqFinanceBaseReq<Object> obj = (MqFinanceBaseReq<Object>) JSON.parseObject(body, new TypeReference<MqFinanceBaseReq<Object>>(){});
            	FinanceInterface instance = financeMqInterface.getInstance(obj.getBusType());
            	BaseResp deal = instance.deal((JSONObject)obj.getData());
            	if(FinanceConstants.SUCCESS==deal.getCode()) {
            		LogUtil.info(logger,message.getMsgID(),"CommitMessage");
                    return Action.CommitMessage;
            	}else {
            		LogUtil.error(logger,message.getMsgID(), "消费失败 : --->" +deal);
            	}
            	
            }
            else {
            	LogUtil.info(logger,message.getMsgID(), "body :" +body);
            	LogUtil.info(logger,message.getMsgID(),"CommitMessage");
                return Action.CommitMessage;
            }
        } catch (Exception e) {
        	LogUtil.error(logger,message.getMsgID(),"ReconsumeLater");
        	LogUtil.error(logger,message.getMsgID(),e.getLocalizedMessage(),e);
            return Action.ReconsumeLater;
        }
        LogUtil.info(logger,message.getMsgID(),"ReconsumeLater");
        return Action.ReconsumeLater;
    }

    @Override
    public void afterPropertiesSet()  {
        configLoader.loadConfig();
        RocketmqManager.consume("payroute", "payorders_2", this);

    }
}
