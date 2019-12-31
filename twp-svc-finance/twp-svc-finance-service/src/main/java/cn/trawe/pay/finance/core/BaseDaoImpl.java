/**  
 * All rights Reserved, Designed By www.trawe.cn
 * @Title:  BaseDaoImpl.java   
 * @Package cn.trawe.pay.finance.common   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: jianjun.chai     
 * @date:   2019年11月11日 下午4:40:50   
 * @version V1.0 
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cn.trawe.pay.finance.core;

import com.alibaba.fastjson.JSON;

import cn.trawe.easyorm.BaseDAO;
import cn.trawe.pay.finance.common.exception.TwFinanceException;

/**   
 * @ClassName:  BaseDaoImpl   
 * @Description:继承ORM BaseDao 自动判断sql是否执行成功,并抛出异常
 * @author: jianjun.chai 
 * @date:   2019年11月11日 下午4:40:50   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
public abstract class BaseDaoImpl<T> extends BaseDAO<T>{

	@Override
	public int save(T t) {
		
		int save = super.save(t);
		if(save <=0) {
			
			throw TwFinanceException.INSERT_RESULT_0.newInstance("数据库操作,insert返回0.实体内容:{%s}", JSON.toJSONString(t));
		}
		return save;
	}

	@Override
	public long saveAndReturnKey(T t) {
		long saveAndReturnKey = super.saveAndReturnKey(t);
		if(saveAndReturnKey <=0) {
			throw TwFinanceException.INSERT_RESULT_0.newInstance("数据库操作,insert返回0.实体内容:{%s}", JSON.toJSONString(t));
		}
		return saveAndReturnKey;
	}

	@Override
	public int update(T t) {
		
		int update = super.update(t);
		if(update <=0) {
            throw TwFinanceException.UPDATE_RESULT_0.newInstance("数据库操作,update返回0.实体内容:{%s}", JSON.toJSONString(t));
		}
		return update;
	}
	
	

	
	
	
	

}
