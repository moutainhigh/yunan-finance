
package cn.trawe.pay.finance.meraccount.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.meraccount.entity.FinMchSubAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserSubAccountBalance;

/**
 * 
 * @ClassName:  FinMchSubAccountBalanceDao   
 * @Description:商户子账户信息表   
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午5:56:56   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
public class FinMchSubAccountBalanceDao extends BaseDaoImpl<FinMchSubAccountBalance>{
	
	
//	public  List<FinMchSubAccountBalance> getDataListByMchAccountNo(String mchAccountNo,String accountType) {
//		
//		List<Object> params = new ArrayList<>();
//        params.add(mchAccountNo);
//        StringBuilder sql = new StringBuilder();
//        sql.append("where mch_account_no = ? ");
//        if(StringUtils.isNotBlank(accountType)) {
//        	sql.append("and account_type = ? ");
//        	params.add(accountType);
//        }
//		List<FinMchSubAccountBalance> find = super.find(sql.toString(), params);
//        return find;
//        
//        
//	}

}
