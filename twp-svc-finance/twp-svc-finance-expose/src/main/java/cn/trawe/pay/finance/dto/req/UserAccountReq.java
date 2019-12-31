/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  UserAccountReq.java   
 * @Package cn.trawe.pay.finance.dto.req   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月18日 上午10:35:41   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.dto.req;

import cn.trawe.pay.finance.dto.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**   
 * @ClassName:  UserAccountReq   
 * @Description:用户账户请求类  
 * @author: jianjun.chai 
 * @date:   2019年11月18日 上午10:35:41   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@ApiModel(value="用户账户请求类")
public class UserAccountReq extends BaseReq{
	
	  
	  //用户注册ID
	  @ApiModelProperty(value="用户ID")
	  private String userId;
      //用户姓名
	  @ApiModelProperty(value="用户姓名")
	  private String userName;
	  
	  //账户类型  枚举请查看:AccountTypeEnum.java
	  @ApiModelProperty(value="账户类型 取值参见AccountTypeEnum.java")
	  private String accountType;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	  
	  

}
