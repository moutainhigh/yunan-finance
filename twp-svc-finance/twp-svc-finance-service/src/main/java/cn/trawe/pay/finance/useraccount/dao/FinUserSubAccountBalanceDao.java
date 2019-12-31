/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  FinUserSubAccountBalanceDao.java   
 * @Package cn.trawe.pay.finance.dao   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月11日 下午4:34:27   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.useraccount.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.useraccount.entity.FinUserSubAccountBalance;

/**   
 * @ClassName:  FinUserSubAccountBalanceDao   
 * @Description:用户子账户信息表
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午4:34:27   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Repository
public class FinUserSubAccountBalanceDao extends BaseDaoImpl<FinUserSubAccountBalance>{
	
	
//	public  List<FinUserSubAccountBalance> getDataListByUserAccountNo(String userAccountNo) {
//		
//		List<Object> params = new ArrayList<>();
//        params.add(userAccountNo);
//		List<FinUserSubAccountBalance> find = super.find("where user_account_no = ? ", params);
//        return find;
//        
//        
//	}
	
//	public  FinUserSubAccountBalance getDataByUserAccountNo(String userAccountNo,String accountType) {
//		
//		List<Object> params = new ArrayList<>();
//        params.add(userAccountNo);
//        params.add(accountType);
//		FinUserSubAccountBalance find = super.findOne("where user_account_no = ? and account_type =?", params);
//        return find;
//        
//        
//	}

}
