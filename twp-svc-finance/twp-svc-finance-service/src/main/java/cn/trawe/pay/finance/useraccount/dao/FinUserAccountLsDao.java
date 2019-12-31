/**
 * All rights Reserved, Designed By www.trawe.cn
 *
 * @Title: FinUserAccountLsDao.java
 * @Package cn.trawe.pay.finance.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: jianjun.chai
 * @date: 2019年11月11日 下午4:30:10
 * @version V1.0
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package cn.trawe.pay.finance.useraccount.dao;

import java.util.*;

import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.enums.TransTypeEnum;
import org.springframework.stereotype.Repository;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;

/**
 * @ClassName: FinUserAccountLsDao
 * @Description:用户账户交易流水表
 * @author: jianjun.chai
 * @date: 2019年11月11日 下午4:30:10
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
public class FinUserAccountLsDao extends BaseDaoImpl<FinUserAccountLs> {

    public FinUserAccountLs getDataByOutOrderNo(String outOrderNo, String outOrderType, Long amount) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_user_account_ls where out_order_no = ? and out_order_type =? and trans_amount =? ");
        List<Object> params = new ArrayList<>();
        params.add(outOrderNo);
        params.add(outOrderType);
        params.add(amount);
        return DAO.wrap(FinUserAccountLs.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();
    }

    public int updateTransStatusByOutOrderNo(String outOrderNo, String incomeExpenses) {
        StringBuilder sql = new StringBuilder();
        sql.append("update fin_user_account_ls set trans_status= ? , update_time=now()  where out_order_no = ? and income_expenses =?");
        List<Object> params = new ArrayList<>();
        params.add(TransStatusEnum.REFUND.getStatus());
        params.add(outOrderNo);
        params.add(incomeExpenses);
        return DAO.wrap(int.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).execute();
    }

    //    查询未结算流水
    public List<FinUserAccountLs> queryNoSettleLs(String accountNo, Date beginTime, Date endTime) {
        List<Object> list = new ArrayList<>();
        list.add(accountNo);
        list.add(TransStatusEnum.NORMAL.getStatus());
        list.add(TransTypeEnum.CONSUME_TYPE.getType());
        list.add(TransTypeEnum.REFOUND_TYPE.getType());
        list.add(beginTime);
        list.add(endTime);
        return this.find(" where account_no = ? and trans_status = ? and trans_type in(?,?) and trans_date >= ? and trans_date< ?", list);
    }

    /**
     * 查询交易数量和交易总金额
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryCountAndTotalTransAmount(String mchAccountNo, Date beginTime, Date endTime) {
        String sql = ("select COUNT(id) AS count, SUM(trans_amount) AS transAmountSum, SUM(mer_service_amount) AS merServiceAmountSum, account_type AS accountType FROM fin_user_account_ls " +
                " where mch_account_no = ? and trans_status = ? and trans_type = ? and trans_date >= ? and trans_date< ? GROUP BY account_type");
        return this.masterJdbcTemplate.queryForList(sql, mchAccountNo, TransStatusEnum.NORMAL.getStatus(), TransTypeEnum.CONSUME_TYPE.getType(), beginTime, endTime);
    }

    /**
     * 更新流水状态
     *
     * @param mchAccountNo   商户编号
     * @param newTransStatus 新的流水状态
     * @param oldTransStatus 旧的流水状态
     * @param beginTime      流水开始时间
     * @param endTime        流水结束时间
     * @return 更新的个数
     */
    public int updateLsStatus(String mchAccountNo, int newTransStatus, int oldTransStatus, Date beginTime, Date endTime) {
        String sql = "update fin_user_account_ls set trans_status= ? , update_time=now() where mch_account_no = ? and trans_type = ? and trans_status =? and trans_date >= ? and trans_date< ? ";
        List<Object> list = new ArrayList<>();
        list.add(newTransStatus);
        list.add(mchAccountNo);
        list.add(TransTypeEnum.CONSUME_TYPE.getType());
        list.add(oldTransStatus);
        list.add(beginTime);
        list.add(endTime);
        return DAO.wrap(int.class, Objects.requireNonNull(masterJdbcTemplate.getDataSource())).query(sql).bind(list).execute();
    }

}
