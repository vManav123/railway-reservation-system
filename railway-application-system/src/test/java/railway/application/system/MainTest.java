package railway.application.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@SpringBootTest
@Import({FunctionalityTesting.class})
class MainTest {

    @Autowired
    private FunctionalityTesting functionalityTesting;

    @Test
    public void ticketCancellation_Testing()
    {
        functionalityTesting.ticketCancellation_TestCase_1();
        functionalityTesting.ticketCancellation_TestCase_2();
        functionalityTesting.ticketCancellation_TestCase_3();
        functionalityTesting.ticketCancellation_TestCase_4();
    }
    @Test
    public void checkPNR_Testing() {
        functionalityTesting.checkPNR_TestCase_1();
        functionalityTesting.checkPNR_TestCase_2();
        functionalityTesting.checkPNR_TestCase_3();
        functionalityTesting.checkPNR_TestCase_4();
    }
    @Test
    public void timeTable_Testing() throws IOException {
        functionalityTesting.trainTimeTable_TestCase_1();
        functionalityTesting.trainTimeTable_TestCase_2();
        functionalityTesting.trainTimeTable_TestCase_3();
        //functionalityTesting.trainTimeTable_TestCase_4();
    }
    @Test
    public void trainBetweenStation_Testing() throws IOException {
        functionalityTesting.trainBetweenStation_TestCase_1();
        functionalityTesting.trainBetweenStation_TestCase_2();
        functionalityTesting.trainBetweenStation_TestCase_3();
        //functionalityTesting.trainBetweenStation_TestCase_4();
    }
    @Test
    public void getTrain_Testing() throws IOException {
        functionalityTesting.getTrain_TestCase_1();
        functionalityTesting.getTrain_TestCase_2();
        functionalityTesting.getTrain_TestCase_3();
        //functionalityTesting.getTrain_TestCase_4();
    }
    @Test
    public void trainFare_Testing() throws IOException {
        functionalityTesting.trainTimeTable_TestCase_1();
        functionalityTesting.trainTimeTable_TestCase_2();
        functionalityTesting.trainTimeTable_TestCase_3();
        //functionalityTesting.trainTimeTable_TestCase_4();
    }
    @Test
    public void reservation_Testing() throws IOException {
        functionalityTesting.trainTimeTable_TestCase_1();
        functionalityTesting.trainTimeTable_TestCase_2();
        functionalityTesting.trainTimeTable_TestCase_3();
        //functionalityTesting.trainTimeTable_TestCase_4();
    }
    @Test
    public void trainLocation_Testing() throws IOException {
        functionalityTesting.trainLocation_TestCase_1();
        functionalityTesting.trainLocation_TestCase_2();
        functionalityTesting.trainLocation_TestCase_3();
        //functionalityTesting.trainLocation_TestCase_4();
    }
    @Test
    public void availableAccommodation_Testing() throws IOException {
        functionalityTesting.trainTimeTable_TestCase_1();
        functionalityTesting.trainTimeTable_TestCase_2();
        functionalityTesting.trainTimeTable_TestCase_3();
        //functionalityTesting.trainTimeTable_TestCase_4();
    }
    @Test
    public void availableSeats_Testing() throws IOException {
        functionalityTesting.availableSeats_TestCase_1();
        functionalityTesting.availableSeats_TestCase_2();
        functionalityTesting.availableSeats_TestCase_3();
        //functionalityTesting.availableSeats_TestCase_3;
    }

}
