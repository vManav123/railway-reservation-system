package bank.mangement.service;

import bank.mangement.service.model.bank.Bank_Account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    // *-------------------- Autowiring Data Content -------------------*
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){return new RestTemplate();}
    @Bean
    public Bank_Account bank_account(){return new Bank_Account();}
    // *----------------------------------------------------------------*

}
