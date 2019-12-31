/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  MchAccountReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午10:42:21   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import cn.trawe.pay.finance.dto.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**   
 * @ClassName:  MchAccountReq   
 * @Description: 商户账户请求类
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午10:42:21   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@ApiModel(value="商户账户请求类")
@Data
public class MchAccountReq   extends BaseReq{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1221854496252792597L;

	//商户ID
	@ApiModelProperty(value="商户ID")
	private String mchId;
	
	//商户姓名
	@ApiModelProperty(value="商户姓名")
	private String  mchName;
	
	/** 结算周期 */

    private String settlementCycle;
    
    @ApiModelProperty(value="账户类型 取值参见AccountTypeEnum.java")
	private String accountType;


	/**
	 * @return the mchId
	 */
	public String getMchId() {
		return mchId;
	}


	/**
	 * @param mchId the mchId to set
	 */
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}


	/**
	 * @return the mchName
	 */
	public String getMchName() {
		return mchName;
	}


	/**
	 * @param mchName the mchName to set
	 */
	public void setMchName(String mchName) {
		this.mchName = mchName;
	}
	
	

}
