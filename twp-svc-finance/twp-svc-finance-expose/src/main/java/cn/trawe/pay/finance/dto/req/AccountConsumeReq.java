/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  AccountConsumeReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午10:46:51   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.trawe.pay.finance.dto.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**   
 * @ClassName:  AccountConsumeReq   
 * @Description:账户消费请求类   
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午10:46:51   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@ApiModel(value="账户消费请求类")
@Data
public class AccountConsumeReq extends BaseReq{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -770891879373681366L;
	
	@ApiModelProperty(value="用户ID")
	@NotBlank(message = "用户ID 不能为空")
	private String userId;
	@ApiModelProperty(value="商户ID")
	@NotBlank(message = "商户ID 不能为空")
	private String mchId;
	@ApiModelProperty(value="账户类型 取值参见AccountTypeEnum.java")
	@NotBlank(message = "账户类型不能为空")
	private String accountType;
	
	@ApiModelProperty(value="交易金额")
	@NotNull( message ="交易金额不能为空")
	private Long amount;
	@ApiModelProperty(value="商户交易手续费金额")
	@NotNull( message ="商户交易手续费金额不能为空")
	private Long merServiceAmount;
	@ApiModelProperty(value="商户手续费率")
	@NotNull( message ="商户手续费率不能为空")
	private String merServiceRate;
	@ApiModelProperty(value="平台交易手续费金额")
	private Long platServiceAmount;
	@ApiModelProperty(value="平台手续费率")
	private String platServiceRate;
	@NotBlank(message = "交易订单号不能为空")
	@ApiModelProperty(value="交易订单号")
	private String outOrderNo;
	@NotBlank(message = "交易订单类型不能为空")
	@ApiModelProperty(value="交易订单类型")
	private String outOrderType;
	@ApiModelProperty(value="交易时间")
	@NotNull( message ="交易时间不能为空")
	private Date transDate;
	@ApiModelProperty(value="通知地址：用户余额支付必填")
	private String notifyUrl;
	
	
	

}
