package cn.trawe.pay.finance.meraccount.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
@Table(name = "fin_mch_account_balance")
public class FinMchAccountBalance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String mchId;

    private String mchName;

    private String accountNo;
    
    private String accountType;
    
    //商户支出手续费
    private Long  merServiceAmount   =0L;
    
    private Long totalBalance =0L;

    private Long freezeBalance=0L;

    private Long useableBalance=0L;

    private Long totalProfit=0L;

    private Date openDate;

    private Date closeDate;

    private int status;
    
    private int virtualAccountStatus;

    private Date createTime = new Date();

    private Date updateTime;

    private String updateOperator ="admin";
    
    /** 结算周期 */
    private String settlementCycle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName == null ? null : mchName.trim();
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
}