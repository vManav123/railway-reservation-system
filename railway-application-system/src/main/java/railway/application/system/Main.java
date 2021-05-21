package railway.application.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import railway.application.system.models.response.Message;
import railway.application.system.models.response.SeatData;
import railway.application.system.models.payment.Payment;
import railway.application.system.models.Ticket;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
    {

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(100000);
        httpRequestFactory.setConnectTimeout(100000);
        httpRequestFactory.setReadTimeout(100000);

        return new RestTemplate(httpRequestFactory);
    }


    // *------------------ Creating beans for Models --------------------*
    @Bean
    public Payment payment() {
        return new Payment();
    }

    @Bean
    public Ticket ticket() {
        return new Ticket();
    }

    @Bean
    public SeatData seatData() {
        return new SeatData();
    }

    @Bean
    public Message message(){return new Message();}
    // *-----------------------------------------------------------------*

}
