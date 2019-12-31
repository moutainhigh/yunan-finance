/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  AccountWithDrawReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午11:05:51   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.trawe.pay.finance.dto.BaseReq;
import cn.trawe.pay.finance.enums.CustomerTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**   
 * @ClassName:  AccountWithDrawReq   
 * @Description:提现请求类  
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午11:05:51   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@ApiModel(value="账户提现请求类")
@Data
public class AccountWithDrawReq extends BaseReq{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3961331071083653563L;
	
	/**
	 *  USER("USER","用户"),
		MCH("MCH","商户")
	 */
	//消费|退款不区分用户类型，默认 USER即可
	private CustomerTypeEnum customerType;
	@ApiModelProperty(value="用户ID")
	private String userId;
	@ApiModelProperty(value="商户ID")
	private String mchId;
	@ApiModelProperty(value="账户类型 取值参见AccountTypeEnum.java")
	private String accountType;
	@ApiModelProperty(value="交易金额")
	@NotNull( message ="交易金额不能为空")
	private Long amount;
	@ApiModelProperty(value="商户交易手续费金额")
	//@NotNull( message ="商户交易手续费金额不能为空")
	private Long merServiceAmount;
	@ApiModelProperty(value="商户手续费率")
	//@NotNull( message ="商户手续费率不能为空")
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

}
