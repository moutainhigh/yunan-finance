package cn.trawe.pay.finance.task;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ptc.board.log.proxy.BizDigestAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 */
@SpringBootApplication(scanBasePackages = {"cn.trawe.pay", "cn.trawe.pay.finance.task", "com.ptc.board.log"}, scanBasePackageClasses = {BizDigestAspect.class})
@EnableDiscoveryClient
@EnableFeignClients
@EnableApolloConfig
@EnableTransactionManagement
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
