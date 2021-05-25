package user.management.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import user.management.system.model.bank.Bank_Account;
import user.management.system.model.user.Credentials;
import user.management.system.model.user.User;

@SpringBootApplication
public class Main {

    public static void main(String[] args) { SpringApplication.run(Main.class, args); }
    @Bean
    public User getUser() {
        return new User();
    }
    @Bean
    public Credentials credentials(){return new Credentials();}
    @Bean
    public Bank_Account getBank_Account() {
        return new Bank_Account();
    }
}
