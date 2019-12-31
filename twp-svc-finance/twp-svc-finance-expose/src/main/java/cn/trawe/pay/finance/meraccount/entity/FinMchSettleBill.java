package cn.trawe.pay.finance.meraccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "fin_mch_settle_bill")
public class FinMchSettleBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchNo;

    private String accountNo;

    private String accountType;

    private Date beginDate;

    private Date endDate;

    private Long incomeAmount;

    private Long expandAmount;
    
    private Long incomeCount;

    private Long transProfit;

    private String billStatus;

    private Date createTime;

    private Date updateTime;

    private String updateOperator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getExpandAmount() {
        return expandAmount;
    }

    public void setExpandAmount(Long expandAmount) {
        this.expandAmount = expandAmount;
    }

    public Long getTransProfit() {
        return transProfit;
    }

    public void setTransProfit(Long transProfit) {
        this.transProfit = transProfit;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus == null ? null : billStatus.trim();
    }

}