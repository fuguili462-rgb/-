package com.itbaizhan.farm_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itbaizhan.farm_system.mapper")
public class FarmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmSystemApplication.class, args);
    }

}
