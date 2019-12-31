/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  TradeUpdateReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午11:24:39   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import java.util.Date;

import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**   
 * @ClassName:  TradeUpdateReq   
 * @Description:流水更新请求类（目前仅支持提现流水的状态更新）
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午11:24:39   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@ApiModel(value="流水更新请求类")
@Data
public class TradeUpdateReq {
	
	
	@ApiModelProperty(value="客户类型 用户 USER  商户 MCH  平台 PLAT")
	private String customerType;
	@ApiModelProperty(value="交易订单号")
	private String outOrderNo;
	@ApiModelProperty(value="交易订单类型")
	private String outOrderType;
	@ApiModelProperty(value="交易时间")
	private Date transDate;
	@ApiModelProperty(value="交易金额")
	private Long amount;
	@ApiModelProperty(value="流水类型")
	private  TransTypeEnum   transType;
	@ApiModelProperty(value="流水状态")
	private  TransStatusEnum  transStatus;
	
	

}
