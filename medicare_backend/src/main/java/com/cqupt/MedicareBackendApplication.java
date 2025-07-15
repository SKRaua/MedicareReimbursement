package com.cqupt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cqupt.mapper")
public class MedicareBackendApplication {
    //本地访问swagger:http://localhost:9999/swagger-ui.html
    public static void main(String[] args) {
        SpringApplication.run(MedicareBackendApplication.class, args);
    }

}
