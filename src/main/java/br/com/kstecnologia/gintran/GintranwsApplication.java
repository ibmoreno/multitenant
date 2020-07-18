package br.com.kstecnologia.gintran;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class GintranwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GintranwsApplication.class, args);
	}

}
