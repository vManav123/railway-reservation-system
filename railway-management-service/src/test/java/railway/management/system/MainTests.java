package railway.management.system;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import railway.management.system.Models.Train;
import railway.management.system.Service.TrainService;

import java.io.IOException;


@SpringBootTest
@Import({Functionality_Testing.class})
class MainTests {

    @Autowired
    TrainService trainService;

    @Autowired
    MongoRepository<Train,Long> mongoRepository;






    // *----------------------- DisplayAllTrain Functionality Testing -------------------*

    @Test
    void DisplayAllTrain_testcase()
    {
        Assertions.assertEquals(mongoRepository.findAll().toString(),trainService.displayAllTrains().toString());
    }

    // *---------------------------------------------------------------------------------*

    // *------------------------ TimeTable Functionality Testing ------------------------*

    @Autowired
    private Functionality_Testing _functionality_testing;

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



//    @Test
//    @BeforeAll
//    void contextLoads() {
//
//    }





}
