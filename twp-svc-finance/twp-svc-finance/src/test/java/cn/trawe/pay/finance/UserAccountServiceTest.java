/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  UserAccountServiceTest.java   
 * @Package cn.trawe.pay.finance   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月12日 下午6:47:56   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.service.impl.TwFinUserAccountTransactionServiceImpl;

/**   
 * @ClassName:  UserAccountServiceTest   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午6:47:56   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public class UserAccountServiceTest extends BaseTest{
	
	
	@Autowired
	TwFinUserAccountTransactionServiceImpl  TwFinUserAccountTransactionServiceImpl;
	
	@Test
	@Transactional
	public void   tese01() {
//		FinUserAccountBalance byUserId = TwFinUserAccountTransactionServiceImpl.getByUserId("20191111", true);
//		System.out.println(JSON.toJSONString(byUserId));
//		FinUserAccountBalance byUserId1 = TwFinUserAccountTransactionServiceImpl.getByUserId("20191111", false);
//		System.out.println(byUserId1);
	}

}
