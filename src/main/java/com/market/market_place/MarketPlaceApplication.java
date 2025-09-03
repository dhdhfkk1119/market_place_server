package com.market.market_place;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 비동기 기능 활성화
@SpringBootApplication
public class MarketPlaceApplication {

	// 주석입니다
	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceApplication.class, args);
	}

}
