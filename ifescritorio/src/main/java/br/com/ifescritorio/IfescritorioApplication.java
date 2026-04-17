package br.com.ifescritorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IfescritorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfescritorioApplication.class, args);
	}

}
