package cn.trawe.pay.finance.useraccount.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountBalance;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName:  FinUserAccountBalanceDao   
 * @Description:用户账户信息表
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午4:35:57   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
@Slf4j
public class FinUserAccountBalanceDao extends BaseDaoImpl<FinUserAccountBalance> {
	
	
	public  FinUserAccountBalance getDataByUserId(String userId,String accountType,boolean isForUpdate) {
		StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_user_account_balance where user_id = ? and account_type =?");
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(accountType);
        if(isForUpdate) {
        	sql.append(" for update");
        }
        return DAO.wrap(FinUserAccountBalance.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();
	}
	
	
	public  FinUserAccountBalance getDataByUserIdAndAccountType(String userId,String accountType) {
		StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_user_account_balance where user_id = ? and account_type = ? ");
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(accountType);
        return DAO.wrap(FinUserAccountBalance.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();
	}
	
	 public int subtractionTotalBalanceAndUseAbleBalance(Long id ,Long amount,String outOrderNo) {
			
		    StringBuilder sql = new StringBuilder();
	        List<Object> params = new ArrayList<>();
	        sql.append("update fin_user_account_balance set total_balance =total_balance - ?,useable_balance = useable_balance - ? ,update_time =? ");
	        params.add(amount);
	        params.add(amount);
	        params.add(new Date());
	        sql.append(" where  id = ?");
	        params.add(id);
	        
	        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
			if(execute <=0) {
				log.error(L.b(outOrderNo).bizType("用户扣减可用余额、总余额失败 主键ID:"+id).s());
	            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.用户扣减可用余额、总余额失败 主键ID:{%s}", id);
			}
			return execute;
		}
	 
	 public int addTotalBalanceAndUseAbleBalance(Long id ,Long amount,String outOrderNo) {
			
		    StringBuilder sql = new StringBuilder();
	        List<Object> params = new ArrayList<>();
	        sql.append("update fin_user_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? , update_time =? ");
	        params.add(amount);
	        params.add(amount);
	        params.add(new Date());
	        sql.append(" where  id = ?");
	        params.add(id);
	        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
			if(execute <=0) {
				log.error(L.b(outOrderNo).bizType("用户增加可用余额、总余额失败 主键ID:"+id).s());
	            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.用户增加可用余额、总余额失败 主键ID:{%s}", id);
			}
			return execute;
		}


	public int changeUserBalance(Long  id ,Long amount,String outOrderNo) {

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sql.append("update fin_user_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? ,update_time =now() ");
		params.add(amount);
		params.add(amount);
		sql.append(" where  id = ?");
		params.add(id);

		int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
		if(execute <=0) {
			log.error(L.b(outOrderNo).bizType("用户更改可用余额、总余额失败 用户ID:"+id).s());
			throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0用户更改可用余额、总余额失败 主键ID:{%s}", id);
		}
		return execute;
	}
	

	
	


}