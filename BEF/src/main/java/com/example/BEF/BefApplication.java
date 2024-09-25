package com.example.BEF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients //feign 클라이언트 등록
public class BefApplication {

	public static void main(String[] args) {
		SpringApplication.run(BefApplication.class, args);
	}

}
