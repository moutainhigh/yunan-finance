package cn.trawe.pay.finance.meraccount.dao;

import java.util.*;

import cn.trawe.pay.finance.enums.AccountStatusEnum;
import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.common.exception.TwFinanceException;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import cn.trawe.util.L;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: FinMchAccountBalanceDao
 * @Description:商户总账户信息表
 * @author: jianjun.chai
 * @date: 2019年11月12日 下午5:54:32
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
@Slf4j
public class FinMchAccountBalanceDao extends BaseDaoImpl<FinMchAccountBalance> {


    public FinMchAccountBalance getDataByMerId(String merId, String accountType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_mch_account_balance where mch_id = ? and account_type =? ");
        List<Object> params = new ArrayList<>();
        params.add(merId);
        params.add(accountType);
        return DAO.wrap(FinMchAccountBalance.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();
    }


    public int addTotalBalanceAndFreezeBalance(Long id,Long addMerAmount, Long merServiceAmount, String outOrderId) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_mch_account_balance set total_balance =total_balance + ?,freeze_balance = freeze_balance + ? "
                + ",mer_service_amount = mer_service_amount + ? " + ", update_time =?  ");
        params.add(addMerAmount);
        params.add(addMerAmount);
        params.add(merServiceAmount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if (execute <= 0) {
            log.error(L.b(outOrderId).msg("商户增加冻结金额、总余额失败 主键ID" + id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.商户增加冻结金额、总余额失败 主键ID:{%s}", id);
        }
        return execute;
    }

    public int addTotalBalanceAndUseAbleBalance(Long id, Long amount, String outOrderId) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_mch_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? ,update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if (execute <= 0) {
            log.error(L.b(outOrderId).msg("商户增加可用余额、总余额失败 主键ID" + id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.商户增加可用余额、总余额失败 主键ID:{%s}", id);
        }
        return execute;
    }

    //商户充值如果收取充值手续费增增加商户支出金额
    public int addTotalBalanceAndUseAbleBalanceAndMerServiceAmount(Long id, Long amount, String outOrderId, Long platServiceAmount) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_mch_account_balance set total_balance =total_balance + ?,useable_balance = useable_balance + ? ,mer_service_amount = mer_service_amount + ?,update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(platServiceAmount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if (execute <= 0) {
            log.error(L.b(outOrderId).msg("商户增加可用余额、总余额失败 主键ID" + id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.商户增加可用余额、总余额失败 主键ID:{%s}", id);
        }
        return execute;
    }

    public int subtractionTotalBalanceAndUseAbleBalance(Long id, Long amount, String outOrderId) {

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_mch_account_balance set total_balance =total_balance - ?,useable_balance = useable_balance - ? ,update_time =?  ");
        params.add(amount);
        params.add(amount);
        params.add(new Date());
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if (execute <= 0) {
            log.error(L.b(outOrderId).msg("商户扣减可用余额、总余额失败 主键ID" + id).s());
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.商户扣减可用余额、总余额失败 主键ID:{%s}", id);
        }
        return execute;
    }

    public int changeFreezeBalance(Map<String, Object> param) {
        Long useableBalance = (Long) param.get("useableBalance");
        Long totalBalance = (Long) param.get("totalBalance");
        Long freezeBalance = (Long) param.get("freezeBalance");
        Long merServiceAmount = (Long) param.get("merServiceAmount");
        String mchId = (String) param.get("mchId");
        Long id = (Long) param.get("id");
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        sql.append("update fin_mch_account_balance set " +
                "useable_balance =useable_balance + ?," +
                "total_balance =total_balance + ?," +
                "freeze_balance = freeze_balance + ? ," +
                "mer_service_amount = mer_service_amount + ? ," +
                "update_time =now() ");
        params.add(useableBalance);
        params.add(totalBalance);
        params.add(freezeBalance);
        params.add(merServiceAmount);
        sql.append(" where  id = ?");
        params.add(id);
        int execute = DAO.wrap(modalClz, getMasterJdbcTemplate().getDataSource()).query(sql.toString()).bind(params).execute();
        if (execute <= 0) {
        	
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("【退款】更新商户账户失败");
        }
        return execute;
    }

    //	查询所有某个日期前入住的所有启用商户
    //只要商户启用了就可以清分
    public List<FinMchAccountBalance> queryAllMchBeforeTime() {
        List<Object> list = new ArrayList<>();
        list.add(AccountStatusEnum.OPEN.getStatus());
        return this.find(" where status = ?", list);
    }

    /**
     * 更新商户结算余额
     *
     * @return
     */
    public int updateSettleBalance(Long settleMoney, String accountNo, String operator) {
        String sql = "update fin_mch_account_balance set freeze_balance = freeze_balance-? , useable_balance = useable_balance+? ,update_time=now(), update_operator = ? where account_no = ? ";
        List<Object> list = new ArrayList<>();
        list.add(settleMoney);
        list.add(settleMoney);
        list.add(operator);
        list.add(accountNo);
        return DAO.wrap(int.class, Objects.requireNonNull(masterJdbcTemplate.getDataSource())).query(sql).bind(list).execute();
    }
}