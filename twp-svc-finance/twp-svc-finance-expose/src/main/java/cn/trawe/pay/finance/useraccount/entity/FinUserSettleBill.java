package cn.trawe.pay.finance.useraccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "fin_user_settle_bill")
public class FinUserSettleBill {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String accountNo;

    private String accountType;

    private Date beginDate;

    private Date endDate;

    private Long incomeAmount;

    private Long expandAmount;

    private Long transProfit;

    private int billStatus;

    private Long serviceChargeAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(int billStatus) {
        this.billStatus = billStatus;
    }

    public Long getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(Long serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }
}