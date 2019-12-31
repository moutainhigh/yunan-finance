/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  RefoundAccountReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午11:17:25   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.trawe.pay.finance.dto.BaseReq;
import cn.trawe.pay.finance.enums.AccountTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**   
 * @ClassName:  RefoundAccountReq   
 * @Description:账户退款请求类   
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午11:17:25   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@ApiModel(value="账户退款请求类")
@Data
public class AccountRefundReq extends BaseReq{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323998400078553745L;
	/**
	 * 
	 */

	
	@ApiModelProperty(value="用户ID")
	private String userId;
	@ApiModelProperty(value="商户ID")
	private String mchId;
	@ApiModelProperty(value="账户类型 取值参见AccountTypeEnum.java")
	private AccountTypeEnum accountType;
	@ApiModelProperty(value="原交易金额")
	@NotNull(message = "原交易金额不能为空")
	private Long amount;
	@ApiModelProperty(value="原商户交易手续费金额")
	@NotNull(message = "原商户交易手续费金额不能为空")
	private Long merServiceAmount;
	@ApiModelProperty(value="原商户手续费率")
	@NotBlank(message ="原商户手续费率不能为空")
	private String merServiceRate;
	@ApiModelProperty(value="平台交易手续费金额")
	private Long platServiceAmount;
	@ApiModelProperty(value="平台手续费率")
	private String platServiceRate;
	@NotBlank(message ="原交易订单号不能为空")
	@ApiModelProperty(value="原交易订单号")
	private String outOrderNo;
	@NotBlank(message ="原交易订单类型不能为空")
	@ApiModelProperty(value="原交易订单类型")
	private String outOrderType;
	@NotNull(message ="原商户手续费率不能为空")
	@ApiModelProperty(value="原交易时间")
	private Date transDate;
	@ApiModelProperty(value="退款交易订单号")
	@NotBlank(message ="退款交易订单号不能为空")
	private String outRefundOrderNo;
	@ApiModelProperty(value="退款交易订单类型")
	@NotBlank(message ="退款交易订单类型不能为空")
	private String outRefundOrderType;
	@NotNull(message ="退款交易时间不能为空")
	@ApiModelProperty(value="退款交易时间")
	private Date outRefundOrderDate;
	@ApiModelProperty(value="通知地址：用户余额支付必填")
	private String notifyUrl;

}
