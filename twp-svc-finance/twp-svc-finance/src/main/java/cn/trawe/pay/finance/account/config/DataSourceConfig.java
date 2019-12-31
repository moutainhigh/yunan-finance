package cn.trawe.pay.finance.account.config;

import cn.trawe.pay.common.config.ProvinceMonthShardingAlgorithm;
import com.alibaba.druid.pool.DruidDataSource;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.ComplexShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DataSourceConfig {


    @Bean
    @ConfigurationProperties(prefix = "master")
    public DataSource getMasterDateSource() {
        return new DruidDataSource();
    }

    @Bean
    @Qualifier("masterDataSource")
    @Primary
    public DataSource masterDataSource() throws SQLException {

        Map<String, DataSource> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put("masterDataSource", getMasterDateSource());

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();

        //配置pay_toll_order_log表规则, 分库 + 分表策略
        TableRuleConfiguration orderLogTableRuleConfig = new TableRuleConfiguration();
        orderLogTableRuleConfig.setLogicTable("pay_toll_order_log");
        orderLogTableRuleConfig.setTableShardingStrategyConfig(new ComplexShardingStrategyConfiguration(
                "entry_province_id,create_time", new ProvinceMonthShardingAlgorithm()));
        shardingRuleConfig.getTableRuleConfigs().add(orderLogTableRuleConfig);


        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());

        return dataSource;
    }

    @Bean(name = "masterJdbcTemplate")
    public JdbcTemplate masterJdbcTemplate(
            @Qualifier("masterDataSource") DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

}
