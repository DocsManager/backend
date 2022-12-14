package com.spring.dm;

import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.spring.service","com.spring.api","com.spring.security", "com.spring.config",  "com.spring.controller","com.spring.util"})
@EntityScan(basePackages = {"com.spring.entity"})
@EnableJpaRepositories(basePackages = {"com.spring.repository"})
@EnableJpaAuditing
//@MapperScan(basePackages = {"com.spring.mapper"})
public class DmApplication {
	
	static {
		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DmApplication.class, args);
	}

}
