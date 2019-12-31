/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  ExceptionHandler.java   
 * @Package cn.trawe.pay.finance.account.exceptionhandler   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月14日 下午3:46:07   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.account.exceptionhandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.FinanceConstants;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.dto.BaseResp;
import lombok.extern.slf4j.Slf4j;

/**   
 * @ClassName:  ExceptionHandler   
 * @Description：异常处理类
 * @author: jianjun.chai 
 * @date:   2019年11月14日 下午3:46:07   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */

@ControllerAdvice
@Slf4j
public class FinaceExceptionHandler {
    
	 @ExceptionHandler({ TwFinanceException.class })
	 @ResponseStatus(HttpStatus.OK)
	 @ResponseBody
	 public BaseResp proBizException (HttpServletRequest request, TwFinanceException e) {
		 
		 
		 BaseResp resp = new BaseResp();
		 resp.setCode(e.getCode());
		 resp.setErrorCode(e.getErrorCode());
		 resp.setMsg(e.getMsg());
		 log.info(JSON.toJSONString(resp));
		 return resp;
	 }
	 
	 @ExceptionHandler({ Exception.class })
	 @ResponseStatus(HttpStatus.OK)
	 @ResponseBody
	 public BaseResp globalException (HttpServletRequest request, Exception e) {
		 
		 log.error(e.getLocalizedMessage(),e.fillInStackTrace());
		 BaseResp resp = new BaseResp();
		 resp.setCode(FinanceConstants.ERROR);
		 resp.setErrorCode(FinanceConstants.ERROR_BUSINESS_CODE);
		 resp.setMsg(e.getLocalizedMessage());
		 log.error(JSON.toJSONString(resp));
		 return resp;
	 }
	
}
