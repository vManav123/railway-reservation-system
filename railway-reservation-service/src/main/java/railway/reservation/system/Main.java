package railway.reservation.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.TaskExecutionListener;
import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import railway.reservation.system.model.ticket.Accommodation;
import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.service.realtimeService.RealTimeService;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableHystrix
@Slf4j
@EnableTask
public class Main implements CommandLineRunner , TaskExecutionListener {

    @Autowired
    private RealTimeService realTimeService;

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

//    @BeforeTask
//    public void beforeTask()
//    {
//        log.info("Task is Going to start soon...");
//    }

    @Override
    public void run(String... args) throws Exception {
        // Run RealtimeTask

        // deleting all OverDue Ticket
        if(realTimeService.any_Ticket_Is_OverDue())
        {
            log.info("Deletion of overdue ticket process result : "+(realTimeService.delete_Overdue_Ticket().equals("success") ? "✔" : "❌"));
        }
        else
        {
            log.info("Overdue Ticket not Found ✔");
        }



        // checking for any waiting Ticket
        if(realTimeService.checkIfAny_Ticket_Is_In_WaitingMode() && realTimeService.checkIfAny_Ticket_Is_Cancelled())
        {
            log.info("Some Waiting Ticket Found ‼");
            log.info("Some Cancelled Seat Found ‼");

            realTimeService.reEvaluate_Ticket();
        }

        log.info("Realtime Process is Finished ✔");
        Thread.sleep(100000);
        log.info("Restarting the RealTime Task ♻");
        run(args);
    }

//    @AfterTask
//    public void afterTask()
//    {
//        log.info("Task is Going to ended...");
//    }

    @Override
    public void onTaskStartup(TaskExecution taskExecution) {
        log.info("Task is Going to start soon...");
    }

    @Override
    public void onTaskEnd(TaskExecution taskExecution) {
        log.info("Task is ended...");
    }

    @Override
    public void onTaskFailed(TaskExecution taskExecution, Throwable throwable) {
        log.error("!!! Task is failed to run !!! ❌");
    }
}
