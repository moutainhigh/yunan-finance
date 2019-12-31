package cn.trawe.pay.finance.account.notify.produce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.RandomAccess;

import org.springframework.stereotype.Component;

import cn.trawe.pay.rocketmq.RocketmqManager;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName:  RocketMqSender   
 * @Description:记账服务RocketMq生产者
 * @author: jianjun.chai 
 * @date:   2019年12月12日 下午3:22:47   
 *     
 * @Copyright: 2019 www.trawe.cn Inc. All rights reserved. 
 * 注意：本内容仅限于北京特微智能科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Slf4j
@Component
public class RocketMqSender {
	
	
	/**
	 * rocketmq 非事务方法
	 * @param msg
	 * @return
	 */
    public static String sendStringMsg(String msg) {
     	 return RocketmqManager.produce("payroute", "payorders_2", msg);
    }
    
    /**
     * rocketmq 事务方法
     * @param msg
     * @return
     */
    //TODO
    public static String sendStringMsgTranstation(String msg) {
    	 return RocketmqManager.produce("payroute", "payorders_2", msg);
    }
    
    public static void main(String args []) {
    	
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(-1);
		arrayList.add(3);
		arrayList.add(3);
		arrayList.add(-5);
		
		arrayList.add(7);
		arrayList.add(4);
		arrayList.add(-9);
		arrayList.add(-7);
		System.out.println("原始数组:");
		System.out.println(arrayList);
		// void reverse(List list)：反转
		//Collections.reverse(arrayList);
		int size = arrayList.size();
//		int j=size-1;
        if (size < 18 || arrayList instanceof RandomAccess) {
            for (int i=0, mid=size>>1,j=size -1 ; i<mid; i++,j--) {
            	
            	
            	System.out.println("i="+i+";"+"mid="+mid+"j="+j);
                Collections.swap(arrayList, i, j);
//                j--;
            }
            
            	
        }
//        Collections.reverse(list);
		System.out.println("Collections.reverse(arrayList):");
		System.out.println(arrayList);
    	
    }

    
}
