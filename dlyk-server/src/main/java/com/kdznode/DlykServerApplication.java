package com.kdznode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kdznode.mapper")
public class DlykServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DlykServerApplication.class, args);
    }

}
