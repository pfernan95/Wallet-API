package com.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")
@EntityScan(basePackages = "com.example")

public class WalletApplication {


    public static void main(String[] args){

        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));  //Indicamos que queremos las fechas con esta zona horaria
        SpringApplication.run(WalletApplication.class,args);

    }

}
