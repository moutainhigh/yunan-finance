
package cn.trawe.pay.finance.plataccount.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.enums.PlatAccountStatusEnum;
import cn.trawe.pay.finance.plataccount.entity.FinPlatSubAccountBalance;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  FinPlatSubAccountBalanceDao   
 * @Description:平台子账户信息表  
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午5:52:12   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
@Slf4j
public class FinPlatSubAccountBalanceDao extends BaseDaoImpl<FinPlatSubAccountBalance>{
	
	public FinPlatSubAccountBalance getData(String accountType){
		List<Object> params = new ArrayList<Object>();
		params.add(accountType);
		FinPlatSubAccountBalance findOne = super.findOne("where account_type = ?", params);
		if(PlatAccountStatusEnum.OPEN.getStatus()!=findOne.getStatus()) {
			 throw TwFinanceException.PLAT_ACCOUNT_RESULT_STATUS_ILLEGAL;
		 }
		return findOne;
		 
	 }
	
	//平台虚拟账户总余额增加金额（充值金额-平台支出手续费），
	//可提现金额增加金额（交易金额-平台手续费金额）、平台支出手续费增加手续费金额（平台和银行交易的手续费金额）
	public int AddTotalBalanceAndUseAbleBalanceAndPlatServiceAmount(Long id ,Long amount,String outOrederNo,Long platServiceAmount) {
		
	    StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_plat_sub_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? ,plat_service_amount = plat_service_amount + ? ,update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(platServiceAmount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
		if(execute <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台总账户子账户增加可用余额、总余额失败 主键ID:{%s}", id);
		}
		return execute;
	}
	
	//平台子账户可用余额扣减提现金额、总余额扣减提现金额
	public int subtractionTotalBalanceAndUseAbleBalance(Long id ,Long amount, String outOrderNo) {
				
	    StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_plat_sub_account_balance set total_balance =total_balance - ?,useable_balance = useable_balance - ?,update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
		if(execute <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台子账户扣减可用余额、总余额、增加在途金额失败 主键ID:{%s}", id);
		}
		return execute;
	}
	
	//平台增加总余额，收入金额，平台收入手续费，平台支出手续费
	
	//不增加平台收入手续费 ，商户已经记录了商户支出和平台收入是一致的，如果记录平台收入，用户余额支付的时候会存在不知道把这笔平台收入手续费添加到哪个账户的情况
//	public int addTotalBalanceAndFreezeBalanceAndMerServiceAmountAndPlatServiceAmount(Long id ,Long amount,Long addPlatSumAmount ,String outOrderNo,Long merServiceAmount,Long platServiceAmount) {
//		
//	    StringBuilder sql = new StringBuilder();
//        List<Object> params = new ArrayList<>();
//        sql.append("update fin_plat_sub_account_balance set total_balance =total_balance + ?,freeze_balance = freeze_balance + ? ,mer_service_amount = mer_service_amount + ? ,plat_service_amount = plat_service_amount + ? ,update_time =?  ");
//        params.add(addPlatSumAmount);
//        params.add(amount);
//        params.add(merServiceAmount);
//        params.add(platServiceAmount);
//        params.add(new Date());
//        sql.append(" where  id = ?");
//        params.add(id);
//        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
//		if(execute <=0) {
//			log.error(L.b(outOrderNo).bizType("平台子账户增加收入余额、总余额、平台收入手续费、平台支出手续费失败 主键ID:"+id).s());
//            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台子账户增加收入余额、总余额、平台收入手续费、平台支出手续费失败主键ID:{%s}", id);
//		}
//		return execute;
//	}
	
	public int addTotalBalanceAndFreezeBalanceAndPlatServiceAmount(Long id ,Long addPlatSumAmount ,String outOrderNo,Long platServiceAmount) {
	
    StringBuilder sql = new StringBuilder();
    List<Object> params = new ArrayList<>();
    sql.append("update fin_plat_sub_account_balance set total_balance =total_balance + ?,freeze_balance = freeze_balance + ? ,plat_service_amount = plat_service_amount + ? ,update_time =?  ");
    params.add(addPlatSumAmount);
    params.add(addPlatSumAmount);
    params.add(platServiceAmount);
    params.add(new Date());
    sql.append(" where  id = ?");
    params.add(id);
    int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
	if(execute <=0) {
		log.error(L.b(outOrderNo).bizType("平台子账户增加收入余额、总余额、平台收入手续费、平台支出手续费失败 主键ID:"+id).s());
        throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台子账户增加收入余额、总余额、平台收入手续费、平台支出手续费失败主键ID:{%s}", id);
	}
	return execute;
}
	
	//增加平台收入手续费金额
    public int addPlatServiceAmount(Long id , String outOrderNo,Long merServiceAmount) {
		
	    StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_plat_sub_account_balance set  mer_service_amount = mer_service_amount + ? , update_time =?  ");
        params.add(merServiceAmount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
		if(execute <=0) {
			log.error(L.b(outOrderNo).bizType("平台子账户增加平台收入手续费失败 主键ID:"+id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台子账户增加平台收入手续费失败 主键ID:{%s}", id);
		}
		return execute;
	}
	
	

    public int changePlatBalance(Map<String,Object> param) {
        Long useableBalance=(Long)param.get("useableBalance");
        Long totalBalance=(Long)param.get("totalBalance");
        Long freezeBalance=(Long)param.get("freezeBalance");
        Long merServiceAmount=(Long)param.get("merServiceAmount");
        Long platServiceAmount=(Long)param.get("platServiceAmount");
        String accountType=(String)param.get("accountType");

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_plat_sub_account_balance set " +
                    "useable_balance =useable_balance + ?," +
                    "total_balance =total_balance + ?," +
                    "freeze_balance = freeze_balance + ?," +
                    "mer_service_amount = mer_service_amount + ?," +
                    "plat_service_amount = plat_service_amount + ?," +
                    "update_time =now() ");
        params.add(useableBalance);
        params.add(totalBalance);
        params.add(freezeBalance);
        params.add(merServiceAmount);
        params.add(platServiceAmount);
        sql.append(" where  account_type = ?");
        params.add(accountType);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if(execute <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.平台总账户子账户更改总余额、收入余额失败 account_type:{%s}", accountType);
        }
        return execute;
    }

}
