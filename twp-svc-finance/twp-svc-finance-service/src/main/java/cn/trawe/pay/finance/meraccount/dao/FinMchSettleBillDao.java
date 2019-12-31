
package cn.trawe.pay.finance.meraccount.dao;

import cn.trawe.easyorm.DAO;
import org.springframework.stereotype.Repository;

import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.meraccount.entity.FinMchSettleBill;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: FinMchSettleBillDao
 * @Description:商户结算信息表
 * @author: jianjun.chai
 * @date: 2019年11月12日 下午5:55:53
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
public class FinMchSettleBillDao extends BaseDaoImpl<FinMchSettleBill> {
    /**
     * 根据批次号 更新记录
     *
     * @param batchNo        批次号
     * @param reviewResult   审核结果
     * @param updateOperator 审核人
     * @return
     */
    public int updateByBatchNo(String batchNo, String reviewResult, String updateOperator) {
        String sql = "update fin_mch_settle_bill set bill_status= ? , update_time=now(), update_operator = ? where batch_no = ? ";
        List<String> list = new ArrayList<>();
        list.add(reviewResult);
        list.add(updateOperator);
        list.add(batchNo);
        return DAO.wrap(int.class, Objects.requireNonNull(masterJdbcTemplate.getDataSource())).query(sql).bind(list).execute();
    }

    /**
     * 根据批次号查询
     *
     * @return
     */
    public List<FinMchSettleBill> queryByBatchNo(String batchNo) {
        List<String> list = new ArrayList<>();
        list.add(batchNo);
        return this.find("where batch_no = ?", list);
    }
}
