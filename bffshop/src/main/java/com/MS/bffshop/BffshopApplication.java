package com.MS.bffshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BffshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BffshopApplication.class, args);
	}

}
