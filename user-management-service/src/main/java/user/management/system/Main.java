package user.management.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import user.management.system.Models.User.User;
import user.management.system.Models.Bank.Bank_Account;

@SpringBootApplication
@EnableEurekaClient
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public User getUser(){return new User();}

    @Bean
    public Bank_Account getBank_Account(){return new Bank_Account();}
}
