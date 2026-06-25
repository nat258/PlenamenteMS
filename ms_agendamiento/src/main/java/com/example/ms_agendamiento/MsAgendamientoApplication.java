package com.example.ms_agendamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsAgendamientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAgendamientoApplication.class, args);
	}

}
