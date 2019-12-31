package cn.trawe.pay.finance.meraccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "fin_mch_sub_account_balance")
public class FinMchSubAccountBalance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String mchAccountNo;

    private String accountNo;

    private String accountName;

    private String accountType;

    private Long incomeAmount =0L;

    private Long expendAmount =0L;

    private Long totalProfit =0L;

    private Date openDate;

    private Date closeDate;

    private int status;

    private int sort  =1 ;

    private Date createTime = new Date();

    private Date updateTime;

    private String updateOperator ="admin";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMchAccountNo() {
        return mchAccountNo;
    }

    public void setMchAccountNo(String mchAccountNo) {
        this.mchAccountNo = mchAccountNo == null ? null : mchAccountNo.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public Long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Long getExpendAmount() {
        return expendAmount;
    }

    public void setExpendAmount(Long expendAmount) {
        this.expendAmount = expendAmount;
    }

    public Long getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(Long totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
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

    public String getUpdateOperator() {
        return updateOperator;
    }

    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator == null ? null : updateOperator.trim();
    }
}