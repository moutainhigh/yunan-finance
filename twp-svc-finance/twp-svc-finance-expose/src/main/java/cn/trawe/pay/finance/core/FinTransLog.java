/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  FinTransLog.java   
 * @Package cn.trawe.pay.finance.core   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年12月2日 下午6:02:24   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.core;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.trawe.pay.finance.meraccount.entity.FinMchAccountBalance;
import lombok.Data;

/**   
 * @ClassName:  FinTransLog   
 * @Description:清分结算日志   
 * @author: jianjun.chai 
 * @date:   2019年12月2日 下午6:02:24   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Data
@Table(name = "fin_trans_log")
public class FinTransLog {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;



    private String outOrderNo;

    private String outOrderType;

    private Date transDate;

    private Long transAmount;

    private int transStatus;

    private String transType;

    private Date createTime = new Date();

    private Date updateTime;
    
    private Long totalBalance;
	
	

}
