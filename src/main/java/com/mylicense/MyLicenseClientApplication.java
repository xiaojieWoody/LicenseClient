package com.mylicense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class MyLicenseClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyLicenseClientApplication.class, args);
	}

}
