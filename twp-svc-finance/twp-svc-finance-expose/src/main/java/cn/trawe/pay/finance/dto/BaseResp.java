/**
 * All rights Reserved, Designed By www.trawe.cn
 *
 * @Title: BaseResp.java
 * @Package cn.trawe.pay.finance.dto
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: jianjun.chai
 * @date: 2019年11月14日 下午3:28:11
 * @version V1.0
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package cn.trawe.pay.finance.dto;

import java.io.Serializable;

import cn.trawe.pay.finance.enums.ErrorCodeEnum;
import com.alibaba.fastjson.JSON;

import cn.trawe.pay.finance.FinanceConstants;

/**
 * @ClassName: BaseResp
 * @Description:清分结算项目传输基类
 * @author: jianjun.chai
 * @date: 2019年11月14日 下午3:28:11
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved.
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
public class BaseResp<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7974676054008567644L;

    /**
     * 异常信息
     */
    protected String msg;

    /**
     * 结果码
     * 0 是成功  1 失败
     */
    protected int code;


    /**
     * 业务错误码
     */
    protected int errorCode;

    T data;


    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }


    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 同时设置错误码和错误信息
     *
     * @param errorCodeEnum
     */
    public void setCodeAndMsg(ErrorCodeEnum errorCodeEnum) {
        if (errorCodeEnum == null) {
            return;
        }
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMsg();
    }


    /**
     * @param msg
     * @param code
     * @param errorCode
     * @param data
     */
    public BaseResp(int code, int errorCode, String msg, T data) {
        super();
        this.msg = msg;
        this.code = code;
        this.errorCode = errorCode;
        this.data = data;
    }


    /**
     * @return the data
     */
    public T getData() {
        return data;
    }


    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }


    /**
     *
     */
    public BaseResp() {
        super();
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static BaseResp successInstance() {
        return new BaseResp(FinanceConstants.SUCCESS, FinanceConstants.SUCCESS_BUSINESS_CODE, "成功", null);
    }

    public static BaseResp errorInstance(String message) {
        return new BaseResp(FinanceConstants.ERROR, FinanceConstants.ERROR_BUSINESS_CODE, message, null);
    }


    public static void main(String[] args) {
        System.out.println(new BaseResp(FinanceConstants.SUCCESS, FinanceConstants.SUCCESS_BUSINESS_CODE, FinanceConstants.SUCCESS_BUSINESS_DESC, new Object()).toString());

    }


}
