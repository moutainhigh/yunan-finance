package cn.trawe.pay.finance;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.ptc.board.log.proxy.BizDigestAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication(scanBasePackages = {"cn.trawe.pay", "com.ptc.board.log" }, scanBasePackageClasses = { BizDigestAspect.class })
@EnableDiscoveryClient
@EnableFeignClients
@EnableApolloConfig
public class FinanceStarter {

    public static void main(String[] args) {
        SpringApplication.run(FinanceStarter.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
