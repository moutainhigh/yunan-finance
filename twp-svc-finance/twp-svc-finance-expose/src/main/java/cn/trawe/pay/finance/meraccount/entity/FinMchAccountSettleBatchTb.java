package cn.trawe.pay.finance.meraccount.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 商户结算批次 表
 * @author liguangyan
 * @date 2019/11/28 11:04
 */
@Data
@Table(name = "fin_mch_account_settle_batch")
public class FinMchAccountSettleBatchTb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;

    private Date batchVersion;
    
    private String batchNo;

    private Integer status;
    
    private Date beginDate;
    
    private Date endDate;

    private Date createTime;

    private Date updateTime;

    private String reason;

}
