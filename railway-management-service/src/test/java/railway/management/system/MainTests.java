package railway.management.system;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import railway.management.system.Models.Train;
import railway.management.system.Repository.TrainRepository;
import railway.management.system.Service.TrainService;

import java.io.IOException;


@SpringBootTest
@Import(TimeTable_Functionality_Testing.class)
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
    TimeTable_Functionality_Testing timeTable_functionality_testing;

    @Test
    public void timeTable_functionality_testing() throws JSONException, IOException {
        timeTable_functionality_testing.TimeTable_testcase_1();
        timeTable_functionality_testing.TimeTable_testcase_2();
        timeTable_functionality_testing.TimeTable_testcase_3();
        timeTable_functionality_testing.TimeTable_testcase_4();
    }

    // *--------------------------------------------------------------------------------*

//    @Test
//    @BeforeAll
//    void contextLoads() {
//
//    }





}
