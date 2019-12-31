
package cn.trawe.pay.finance.plataccount.dao;

import cn.trawe.easyorm.DAO;
import cn.trawe.pay.finance.enums.TransStatusEnum;
import cn.trawe.pay.finance.meraccount.entity.FinMchAccountLs;
import cn.trawe.pay.finance.useraccount.entity.FinUserAccountLs;
import org.springframework.stereotype.Repository;

import cn.trawe.pay.finance.core.BaseDaoImpl;
import cn.trawe.pay.finance.plataccount.entity.FinPlatAccountLs;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName:  FinPlatAccountLsDao   
 * @Description:平台账户流水   
 * @author: jianjun.chai 
 * @date:   2019年11月12日 下午5:50:32   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Repository
public class FinPlatAccountLsDao extends BaseDaoImpl<FinPlatAccountLs> {
    public FinPlatAccountLs getDataByOutOrderNo(String outOrderNo, String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from fin_plat_account_ls where out_order_no = ? and income_expenses = ? ");
        List<Object> params = new ArrayList<>();
        params.add(outOrderNo);
        params.add(type);
        return DAO.wrap(FinPlatAccountLs.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).first();

    }

    public int updateTransStatusByOutOrderNo(String outOrderNo,String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("update fin_plat_account_ls set trans_status= ?  , update_time=now()   where out_order_no = ?and income_expenses =? ");
        List<Object> params = new ArrayList<>();
        params.add(TransStatusEnum.REFUND.getStatus());
        params.add(outOrderNo);
        params.add(type);
        return DAO.wrap(int.class, masterJdbcTemplate.getDataSource()).query(sql.toString()).bind(params).execute();
    }
}