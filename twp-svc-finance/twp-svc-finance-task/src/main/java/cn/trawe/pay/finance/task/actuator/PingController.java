/**
 * PingController.java
 * 2018年3月27日
 * ©2015-2018 北京特微智能科技有限公司. All rights reserved.
 */
package cn.trawe.pay.finance.task.actuator;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 基本响应URL
 * @author wangda
 */
@RestController
public class PingController {
    
    @GetMapping(path="/ping")
    public String ping() {
        return "pong";
    }
    
}
