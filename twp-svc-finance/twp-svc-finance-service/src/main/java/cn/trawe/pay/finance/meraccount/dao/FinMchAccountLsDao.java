
package cn.trawe.pay.finance.meraccount.dao;

import java.util.*;

import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;

/**
 * @ClassName: FinMchAccountLsDao
 * @Description:商户账户交易流水表
 * @author: jianjun.chai
 * @date: 2019年11月12日 下午5:55:10
 * @Copyright: 2019 www.trawe.cn Inc. All r	ights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
public class FinMchAccountLsDao extends BaseDaoImpl<FinMchAccountLs> {

    public FinMchAccountLs getDataByOutOrderNo(String outOrderNo, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_mch_account_ls where out_order_no = ? and income_expenses = ? ");
        List<Object> params = new ArrayList<>();
        params.add(outOrderNo);
        params.add(type);
        return DAO.wrap(FinMchAccountLs.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();
    }

    public int updateTransStatusByOutOrderNo(String outOrderNo, String inComeExpense) {
        StringBuilder sql = new StringBuilder();
        sql.append("update fin_mch_account_ls set trans_status= ? , update_time=now()  where out_order_no = ?  and income_expenses =? ");
        List<Object> params = new ArrayList<>();
        params.add(TransStatusEnum.REFUND.getStatus());
        params.add(outOrderNo);
        params.add(inComeExpense);
        return DAO.wrap(int.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).execute();
    }

    //    查询未结算流水
    public List<FinMchAccountLs> queryNoSettleLs(String accountNo, Date beginTime, Date endTime) {
        List<Object> list = new ArrayList<>();
        list.add(accountNo);
        list.add(TransStatusEnum.NORMAL.getStatus());
        list.add(TransTypeEnum.CONSUME_TYPE.getType());
        list.add(TransTypeEnum.REFOUND_TYPE.getType());
        list.add(beginTime);
        list.add(endTime);
        return this.find("where account_no = ? and trans_status = ? and trans_type in(?,?) and trans_date >= ? and trans_date< ?", list);
    }

    /**
     * 查询交易数量和交易总金额
     *
     * @param accountNo
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryCountAndTotalTransAmount(String accountNo, Date beginTime, Date endTime) {
        String sql = ("select COUNT(id) AS count, SUM(trans_amount) AS transAmountSum, SUM(mer_service_amount) AS merServiceAmountSum, account_type AS accountType FROM fin_mch_account_ls " +
                " where account_no = ? and trans_status = ? and trans_type = ? and trans_date >= ? and trans_date< ? GROUP BY account_type");
        return this.masterJdbcTemplate.queryForList(sql, accountNo, TransStatusEnum.NORMAL.getStatus(), TransTypeEnum.CONSUME_TYPE.getType(), beginTime, endTime);
    }

    /**
     * 更新流水状态
     *
     * @param accountNo      商户编号
     * @param newTransStatus 新的流水状态
     * @param oldTransStatus 旧的流水状态
     * @param beginTime      流水开始时间
     * @param endTime        流水结束时间
     * @return
     */
    public int updateLsStatus(String accountNo, int newTransStatus, int oldTransStatus, Date beginTime, Date endTime) {
        String sql = "update fin_mch_account_ls set trans_status= ? , update_time=now() where account_no = ? and trans_type = ? and trans_status =? and trans_date >= ? and trans_date< ? ";
        List<Object> list = new ArrayList<>();
        list.add(newTransStatus);
        list.add(accountNo);
        list.add(TransTypeEnum.CONSUME_TYPE.getType());
        list.add(oldTransStatus);
        list.add(beginTime);
        list.add(endTime);
        return DAO.wrap(int.class, Objects.requireNonNull(masterJdbcTemplate.getDataSource())).query(sql).bind(list).execute();
    }

}
