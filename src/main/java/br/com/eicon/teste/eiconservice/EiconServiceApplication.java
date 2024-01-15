package br.com.eicon.teste.eiconservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EiconServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EiconServiceApplication.class, args);
	}
}
