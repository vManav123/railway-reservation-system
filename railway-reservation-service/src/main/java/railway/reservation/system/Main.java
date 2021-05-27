package railway.reservation.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.model.ticket.Accommodation;
import railway.reservation.system.model.ticket.ReservedTicket;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@Slf4j
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ReservedTicket getReserveTicket() {
        return new ReservedTicket();
    }

    @Bean
    public Accommodation getAccommodation() {
        return new Accommodation();
    }
}
