package railway.reservation.system;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.service.trainService.TrainService;

import java.io.IOException;


@SpringBootTest
@Import({Functionality_Testing.class})
class MainTests {

    @Autowired
    TrainService trainService;

    @Autowired
    MongoRepository<Train, Long> mongoRepository;


    // *----------------------- DisplayAllTrain Functionality Testing -------------------*
    @Autowired
    private Functionality_Testing _functionality_testing;

    @Test
    void DisplayAllTrain_testcase() {
        Assertions.assertEquals(mongoRepository.findAll().toString(), trainService.displayAllTrains().toString());
    }
    // *---------------------------------------------------------------------------------*


    // *------------------------ TimeTable Functionality Testing ------------------------*
    @Test
    public void timeTable_functionality_testing() throws JSONException, IOException {
        _functionality_testing.TimeTable_testcase_1();
        _functionality_testing.TimeTable_testcase_2();
        _functionality_testing.TimeTable_testcase_3();
        _functionality_testing.TimeTable_testcase_4();
    }
    // *--------------------------------------------------------------------------------*


    // *----------------- train Between Station Functionality Testing ------------------*
    @Test
    public void trainsBetweenStation_functionality_testing() throws JSONException, IOException {
        _functionality_testing.TrainsBetweenStation_testcase_1();
        _functionality_testing.TrainsBetweenStation_testcase_2();
        _functionality_testing.TrainsBetweenStation_testcase_3();
        _functionality_testing.TrainsBetweenStation_testcase_4();
    }
    // *---------------------------------------------------------------------------------*


    // *----------------- train Between Station Functionality Testing -------------------*
    @Test
    public void trainFare_functionality_testing() throws JSONException, IOException{
        _functionality_testing.TrainsFare_testcase_1();
        _functionality_testing.TrainsFare_testcase_2();
        _functionality_testing.TrainsFare_testcase_3();
        _functionality_testing.TrainsFare_testcase_4();
    }
    // *---------------------------------------------------------------------------------*


    // *----------------- train Between Station Functionality Testing -------------------*
    @Test
    public void trainDetail_functionality_testing() throws JSONException, IOException{
        _functionality_testing.train_testcase_1();
        _functionality_testing.train_testcase_2();
        _functionality_testing.train_testcase_3();
        _functionality_testing.train_testcase_4();
    }
    // *---------------------------------------------------------------------------------*

    // *----------------- train Between Station Functionality Testing -------------------*
    @Test
    public void ticketCancellation_functionality_testing() throws JSONException, IOException{
        _functionality_testing.ticketCancellation_TestCase_1();
        _functionality_testing.ticketCancellation_TestCase_2();
        _functionality_testing.ticketCancellation_TestCase_3();
        _functionality_testing.ticketCancellation_TestCase_4();
    }
    // *---------------------------------------------------------------------------------*



}
