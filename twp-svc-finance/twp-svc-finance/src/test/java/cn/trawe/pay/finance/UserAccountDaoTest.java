/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  UserAccountDaoTest.java   
 * @Package cn.trawe.pay.finance   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月11日 下午5:35:44   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.trawe.pay.finance.enums.AccountTypeEnum;
import cn.trawe.pay.finance.useraccount.dao.FinUserAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.dao.FinUserSubAccountBalanceDao;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.pay.finance.useraccount.entity.FinUserSubAccountBalance;

/**   
 * @ClassName:  UserAccountDaoTest   
 * @Description:用户相关表测试类 
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午5:35:44   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */



public class UserAccountDaoTest  extends BaseTest{
	
	
	    @Autowired
	    private FinUserAccountBalanceDao   FinUserAccountBalanceDao;
	    
	    @Autowired
	    private FinUserSubAccountBalanceDao   FinUserSubAccountBalanceDao;
		
		
		/**
		 * 
		 * @Title: insert_useraccount   
		 * @Description: 插入用户总账户信息(事务控制)   
		 * @param:       
		 * @return: void      
		 * @throws
		 */
		@Test
		@Transactional
		public void insert_useraccount() {

			//FinUserAccountBalance findByKeyName = FinUserAccountBalanceDao.findByKeyName("id", 1);
			//findByKeyName.setTotalBalance(20L);
			//FinUserAccountBalanceDao.update(findByKeyName);
			FinUserAccountBalance account = new FinUserAccountBalance();
			account.setAccountNo("201911110002");
			account.setCloseDate(new Date());
			account.setCreateTime(new Date());
			account.setOpenDate(new Date());
			account.setStatus((byte) 0);
			account.setTotalBalance(10L);
			account.setTotalProfit(10L);
			account.setFreezeBalance(10L);
			account.setUpdateOperator("jianjun.chai");
			account.setUpdateTime(new Date());
			account.setUserId("20191112");
			account.setUseableBalance(10L);
			account.setUserName("柴建军");
			//int save = FinUserAccountBalanceDao.save(account);
			//System.out.println(save);
			
		}
		
		/**
		 * 
		 * @Title: insert_user_sub_account   
		 * @Description: 插入用户子账户信息
		 * @param:       
		 * @return: void      
		 * @throws
		 */
		@Test
		public void insert_user_sub_account() {
			FinUserSubAccountBalance  userSubAccount = new FinUserSubAccountBalance();
			userSubAccount.setAccountName("jianjun.chai.sub");
			userSubAccount.setUserAccountNo("20191111");
			userSubAccount.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
			userSubAccount.setAccountNo("20191112");
			userSubAccount.setCloseDate(new Date());
			userSubAccount.setCreateTime(new Date());
			userSubAccount.setExpendAmount(10L);
			userSubAccount.setIncomeAmount(10L);
			userSubAccount.setOpenDate(new Date());
			userSubAccount.setSort((byte)0);
			userSubAccount.setStatus((byte)0);
			userSubAccount.setTotalProfit(10L);
			userSubAccount.setUpdateOperator("admin");
			userSubAccount.setUpdateTime(new Date());
			//long saveAndReturnKey = FinUserSubAccountBalanceDao.saveAndReturnKey(userSubAccount);
			//System.out.println(saveAndReturnKey);

		}
		
		/**
		 * 
		 * @Title: insert_user_sub_account   
		 * @Description: 插入用户子账户信息(事务控制）
		 * @param:       
		 * @return: void      
		 * @throws
		 */
		@Test
		@Transactional
		public void insert_user_sub_account_trans() {
			
			FinUserSubAccountBalance  userSubAccount = new FinUserSubAccountBalance();
			userSubAccount.setAccountName("jianjun.chai.sub");
			userSubAccount.setUserAccountNo("x");
			userSubAccount.setAccountType(AccountTypeEnum.PLAT_ACCOUNT.getAccount());
			userSubAccount.setAccountNo("20191112");
			userSubAccount.setCloseDate(new Date());
			userSubAccount.setCreateTime(new Date());
			userSubAccount.setExpendAmount(10L);
			userSubAccount.setIncomeAmount(10L);
			userSubAccount.setOpenDate(new Date());
			userSubAccount.setSort((byte)0);
			userSubAccount.setStatus((byte)0);
			userSubAccount.setTotalProfit(10L);
			userSubAccount.setUpdateOperator("admin");
			userSubAccount.setUpdateTime(new Date());
			//long saveAndReturnKey = FinUserSubAccountBalanceDao.saveAndReturnKey(userSubAccount);
			//System.out.println(saveAndReturnKey);
			insert_useraccount();
		}
		
		/**
		 * 
		 * @Title: insert_user_account_ls   
		 * @Description: 插入用户账户交易流水 
		 * @param:       
		 * @return: void      
		 * @throws
		 */
		@Test
		public void insert_user_account_ls() {
			
		}

}
