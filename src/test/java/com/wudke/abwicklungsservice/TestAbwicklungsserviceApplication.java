package com.wudke.abwicklungsservice;

import org.springframework.boot.SpringApplication;

public class TestAbwicklungsserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(AbwicklungsserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
