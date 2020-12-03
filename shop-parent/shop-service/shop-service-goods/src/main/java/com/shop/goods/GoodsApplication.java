package com.shop.goods;

import com.shop.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.shop.goods.dao"})
@EnableTransactionManagement(proxyTargetClass = true)
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    /****
     * used to generate non-duplicated ids
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0, 0);
    }
}
