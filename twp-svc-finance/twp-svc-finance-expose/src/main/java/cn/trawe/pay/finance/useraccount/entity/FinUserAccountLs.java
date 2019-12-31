package cn.trawe.pay.finance.useraccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Table(name = "fin_user_account_ls")
public class FinUserAccountLs {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String incomeExpenses;
	
    private String accountNo;

    private String accountType;

    private String mchAccountNo;

    private String transNo;

    private String outOrderNo;

    private String outOrderType;

    private Date transDate;

    private Long transAmount;

    private Long merServiceAmount;

    private String merServiceRate;
    
    private Long platServiceAmount;

    private String platServiceRate;

    private int transStatus;

    private String transType;
    
    private Date createTime = new Date();
    
    private Date updateTime;
    
    private Long totalBalance;
    
    private String memo;

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

    public String getMchAccountNo() {
        return mchAccountNo;
    }

    public void setMchAccountNo(String mchAccountNo) {
        this.mchAccountNo = mchAccountNo == null ? null : mchAccountNo.trim();
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo == null ? null : transNo.trim();
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo == null ? null : outOrderNo.trim();
    }

    public String getOutOrderType() {
        return outOrderType;
    }

    public void setOutOrderType(String outOrderType) {
        this.outOrderType = outOrderType == null ? null : outOrderType.trim();
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public Long getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(Long transAmount) {
        this.transAmount = transAmount;
    }



    public int getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(int transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType == null ? null : transType.trim();
    }
}