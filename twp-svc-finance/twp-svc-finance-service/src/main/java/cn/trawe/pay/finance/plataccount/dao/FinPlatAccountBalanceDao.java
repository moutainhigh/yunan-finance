package cn.trawe.pay.finance.plataccount.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.enums.PlatAccountStatusEnum;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountBalance;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName:  FinPlatAccountBalanceDao   
 * @Description:  平台总账户
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午5:46:51   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
@Slf4j
public class FinPlatAccountBalanceDao extends BaseDaoImpl<FinPlatAccountBalance> {
	
	
	
	//获取总账户信息
	public FinPlatAccountBalance getData(){
		
		
		 List<FinPlatAccountBalance> findAll = super.findAll();
		 FinPlatAccountBalance finPlatAccountBalance = findAll.get(0);
		 if(PlatAccountStatusEnum.OPEN.getStatus()!=finPlatAccountBalance.getStatus()) {
			 throw TwFinanceException.PLAT_ACCOUNT_RESULT_STATUS_ILLEGAL;
		 }
		 return finPlatAccountBalance;
		
	}
	
	//平台总账户总余额、可用余额增加对应金额
	public int AddTotalBalanceAndUseAbleBalanceAndPlatServiceAmount(Long id ,Long amount,String outOrderNo,Long platServiceAmount) {
		
	    StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_plat_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? ,plat_service_amount = plat_service_amount + ? ,set update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(platServiceAmount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
		if(execute <=0) {
			log.error(L.b(outOrderNo).bizType("平台总账户增加可用余额、总余额失败 主键ID:"+id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台总账户增加可用余额、总余额、平台手续费失败 主键ID:{%s}", id);
		}
		return execute;
	}
	
	    //平台总账户冻结余额增加对应金额（交易金额-商户手续费），商户手续费金额字段增加商户手续费金额（平台收入）、
	       //平台手续费字段增加（银行手续费金额、平台支出）、总余额增加对应金额（交易金额-商户手续费）
		public int addTotalBalanceAndFreezeBalanceAndMerServiceAmountAndPlatServiceAmount(Long id ,Long amount,String outOrderNo,Long merServiceAmount,Long platServiceAmount) {
			
		    StringBuilder sql = new StringBuilder();
	        List<Object> params = new ArrayList<>();
	        sql.append("update fin_plat_account_balance set total_balance =total_balance + ?,freeze_balance = freeze_balance + ? ,mer_service_amount = mer_service_amount + ? ,plat_service_amount = plat_service_amount + ? ,set update_time =?  ");
	        params.add(amount);
	        params.add(amount);
	        params.add(merServiceAmount);
	        params.add(platServiceAmount);
	        params.add(new Date());
	        sql.append(" where  id = ?");
	        params.add(id);
	        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
			if(execute <=0) {
				log.error(L.b(outOrderNo).bizType("平台总账户增加冻结余额、总余额、商户手续费、平台手续费失败 主键ID:"+id).s());
	            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台总账户增加冻结余额、总余额、商户手续费、平台手续费失败 主键ID:{%s}", id);
			}
			return execute;
		}
	
	//平台总账户可用余额扣减提现金额、总余额扣减提现金额、在途金额增加提现金额、增加提现手续费字段
	public int subtractionTotalBalanceAndUseAbleBalanceAddcashInTransitAndPlateServiceAmount(Long id ,Long amount,Long cashInTransitAmount,String outOrderNo,Long plateServiceAmount) {
			
		    StringBuilder sql = new StringBuilder();
	        List<Object> params = new ArrayList<>();
	        sql.append("update fin_plat_account_balance set total_balance =total_balance - ?,useable_balance = useable_balance - ? ,cash_in_transit =cash_in_transit + ?,plat_service_amount =plat_service_amount+ ?,set update_time =?  ");
	        params.add(amount);
	        params.add(amount);
	        params.add(cashInTransitAmount);
	        params.add(plateServiceAmount);
	        params.add(new Date());
	        sql.append(" where  id = ?");
	        params.add(id);
	        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
			if(execute <=0) {
				log.error(L.b(outOrderNo).bizType("平台总账户扣减可用余额、总余额、增加在途金额失败 主键ID:"+id).s());
	            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台总账户扣减可用余额、总余额、增加在途金额失败 主键ID:{%s}", id);
			}
			return execute;
	}
	


}