/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  TwDaoException.java   
 * @Package cn.trawe.pay.finace.common.exception   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月11日 下午4:53:21   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.common.exception;

/**   
 * @ClassName:  TwDaoException   
 * @Description:清分结算平台异常基类  
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午4:53:21   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public class TwFinanceException extends RuntimeException {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4792683674735422871L;

	/**
     * insert返回0
     */
    public static final TwFinanceException INSERT_RESULT_0 = new TwFinanceException(1,
            90001, "insert返回0");

    /**
     * update返回0
     */
    public static final TwFinanceException UPDATE_RESULT_0 = new TwFinanceException(1,
            90002, "update返回0");
    
    /**
         * 用户账户信息未查询到
     */
    public static final TwFinanceException ACCOUNT_RESULT_0 = new TwFinanceException(1,
            10001, " 用户账户信息未查询到");
    
    /**
     *   用户账户状态不合法
     */
    public static final TwFinanceException ACCOUNT_RESULT_STATUS_ILLEGAL = new TwFinanceException(1,
        10002, "用户账户状态不合法");
    
    /**
     *   用户账户可用余额不足
     */
    public static final TwFinanceException ACCOUNT_BALANCE_INSUFFICIENT = new TwFinanceException(1,
        10003, "用户可用余额不足");
    
    
    
    
    /**
     *   商户账户不存在
     */
    public static final TwFinanceException MER_ACCOUNT_RESULT_0 = new TwFinanceException(1,
        20001, "商户账户信息未查询到");
    
    /**
     *   商户账户状态不合法
     */
    public static final TwFinanceException MER_ACCOUNT_RESULT_STATUS_ILLEGAL = new TwFinanceException(1,
        20002, "商户账户状态不合法");
    
    public static final TwFinanceException MER_ACCOUNT_RECHARGE_SUCCESSED = new TwFinanceException(0,
            20003, "原交易已成功");

    /**
     *   商户交易流水不存在
     */
    public static final TwFinanceException MER_ACCOUNT_Ls_0 = new TwFinanceException(1,
            20004, "商户交易流水不存在");

    /**
     *   商户余额不足
     */
    public static final TwFinanceException MER_ACCOUNT_NOMONEY_0 = new TwFinanceException(1,
            20005, "商户余额不足");

    /**
     *   该订单已经退款
     */
    public static final TwFinanceException MER_ACCOUNT_REFUNDED_0 = new TwFinanceException(0,
            20006, "该订单已经退款");

    /**
     *   该商户流水状态异常
     */
    public static final TwFinanceException MER_ACCOUNT_UNKONW_LS_0 = new TwFinanceException(1,
            20007, "该商户流水状态异常");
   
    /**
         *   平台账户状态不合法
     */
    public static final TwFinanceException PLAT_ACCOUNT_RESULT_STATUS_ILLEGAL = new TwFinanceException(1,
        30002, "平台账户状态不合法");
    
    /**
     *  平台总账户可用余额不足
     */
    public static final TwFinanceException PLAT_ACCOUNT_BALANCE_INSUFFICIENT = new TwFinanceException(1,
        30003, "平台账户可用余额不足");
    
    /**
     *  平台子账户可用余额不足
     */
    public static final TwFinanceException PLAT_SUB_ACCOUNT_BALANCE_INSUFFICIENT = new TwFinanceException(1,
        30003, "平台子账户可用余额不足");
    
    
    /**
     *  不支持的业务类型
     */
    public static final TwFinanceException BUSSINESS_ERROR = new TwFinanceException(1,
        40001, "不支持的业务类型");
    
    /**
     *  流水不存在
     */
    public static final TwFinanceException TRADE_NOT_FOUND_ERROR = new TwFinanceException(1,
        40002, "未查询到流水");
    
    /**
     * 异常信息
     */
    protected String msg;

    /**
          *  结果码
     */
    protected int code;
    
    
    /**
     * 业务错误码
     */
    protected int errorCode;
    
    /**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}



	public TwFinanceException(int code, int errorCode,String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
        this.errorCode =errorCode;
        this.msg = String.format(msgFormat, args);
    }
    
    
    public TwFinanceException newInstance(String msgFormat, Object... args) {
        return new TwFinanceException(this.code,this.errorCode, msgFormat, args);
    }

    public TwFinanceException() {
        super();
    }

    public TwFinanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwFinanceException(Throwable cause) {
        super(cause);
    }

    public TwFinanceException(String message) {
        super(message);
    }
    
    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
    
    


}
