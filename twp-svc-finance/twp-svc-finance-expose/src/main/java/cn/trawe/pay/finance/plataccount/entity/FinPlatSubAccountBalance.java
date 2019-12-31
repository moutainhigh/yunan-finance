package cn.trawe.pay.finance.plataccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "fin_plat_sub_account_balance")
public class FinPlatSubAccountBalance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String platAccountNo;

    private String accountNo;

    private String accountName;

    private String accountType;

    private Long totalBalance;

    private Long freezeBalance;

    private Long useableBalance;

    private Long totalProfit;

    private Date openDate;

    private Date closeDate;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private String updateOperator;

    private Long cashInTransit;

    private Long merServiceAmount;
    
    private Long platServiceAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatAccountNo() {
        return platAccountNo;
    }

    public void setPlatAccountNo(String platAccountNo) {
        this.platAccountNo = platAccountNo == null ? null : platAccountNo.trim();
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

    public Long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Long getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(Long freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public Long getUseableBalance() {
        return useableBalance;
    }

    public void setUseableBalance(Long useableBalance) {
        this.useableBalance = useableBalance;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public Long getCashInTransit() {
        return cashInTransit;
    }

    public void setCashInTransit(Long cashInTransit) {
        this.cashInTransit = cashInTransit;
    }

  
}