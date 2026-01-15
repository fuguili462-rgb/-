package com.itbaizhan.farm_main;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 百战智慧农业项目的启动类
 * 会统一启动所有模块
 */
@SpringBootApplication(scanBasePackages = {"com.itbaizhan.farm_common",
                        "com.itbaizhan.farm_system"})
@MapperScan(basePackages = {"com.itbaizhan.farm_system.mapper"})
public class FarmMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmMainApplication.class, args);
    }

    /**
     * 添加mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}
