package railway.application.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import railway.application.system.service.applicationService.ApplicationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@TestComponent
public class FunctionalityTesting {

    @Autowired
    ApplicationService applicationService;


    
    
    
    
    
    
    
    
    
    @Test
    public void trainTimeTable_TestCase_1()
    {
        Assertions.assertEquals("There is no Station Exist on the Route Pakistan Rail Network",applicationService.trainTimeTable("Hello world"));
    }
    @Test
    public void trainTimeTable_TestCase_2()
    {
        Assertions.assertEquals("There is no Station Exist on the Route Pakistan Rail Network",applicationService.trainTimeTable("Lahore city"));
    }
    @Test
    public void trainTimeTable_TestCase_3()
    {
        Assertions.assertEquals("There is no Station Exist on the Route Pakistan Rail Network",applicationService.trainTimeTable("Karachi"));
    }
    @Test
    public void trainTimeTable_TestCase_4() throws IOException {
        Assertions.assertEquals(Files.readString(Path.of("src/test/resources/timeTable_Testing.txt")),applicationService.trainTimeTable("Karachi City"));
    }
    
    


    @Test
    public void checkPNR_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.getPNR(123L));
    }
    @Test
    public void checkPNR_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.getPNR(11223L));
    }
    @Test
    public void checkPNR_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.getPNR(123333L));
    }
    @Test
    public void checkPNR_TestCase_4() {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.getPNR(1212313L));
    }




    @Test
    public void ticketCancellation_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(123));
    }
    @Test
    public void ticketCancellation_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(121233));
    }
    @Test
    public void ticketCancellation_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(945453));
    }
    @Test
    public void ticketCancellation_TestCase_4()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(239499));
    }


    @Test
    public void trainBetweenStation_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(123));
    }
    @Test
    public void trainBetweenStation_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(121233));
    }
    @Test
    public void trainBetweenStation_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(945453));
    }
    @Test
    public void trainBetweenStation_TestCase_4()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(239499));
    }

    @Test
    public void getTrain_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(123));
    }
    @Test
    public void getTrain_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(121233));
    }
    @Test
    public void getTrain_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(945453));
    }
    @Test
    public void getTrain_TestCase_4()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(239499));
    }
    @Test
    public void availableSeats_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(123));
    }
    @Test
    public void availableSeats_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(121233));
    }
    @Test
    public void availableSeats_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(945453));
    }
    @Test
    public void availableSeats_TestCase_4()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(239499));
    }
    @Test
    public void trainLocation_TestCase_1()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(123));
    }
    @Test
    public void trainLocation_TestCase_2()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(121233));
    }
    @Test
    public void trainLocation_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(945453));
    }
    @Test
    public void trainLocation_TestCase_4()
    {
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",applicationService.TicketCancellation(239499));
    }

}
