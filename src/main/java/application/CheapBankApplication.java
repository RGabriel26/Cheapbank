 package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
//@ComponentScan(basePackages = {"cp.login","cp.deshboard","cp.tempclass","services"})
@EnableJpaRepositories(basePackages = "repo")
@EntityScan("cp.entity")
@SpringBootApplication(scanBasePackages = {"cp.bank.controller","cp.tempclass","services"})
public class CheapBankApplication {
	
	public static Long loggedID;
	
	public static void main(String[] args) {
		SpringApplication.run(CheapBankApplication.class, args);
	}

}
