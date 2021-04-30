package railway.management.system;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import railway.management.system.Models.TimeTable;
import railway.management.system.Service.TrainService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestConfiguration
public class TimeTable_Functionality_Testing {

    String timeTableFile;
    List<TimeTable> timeTableList;
    JSONArray jsonArray;

    @Autowired
    TrainService trainService;





    // TimeTable Functionality
    List<TimeTable> getTimeTableList(String timeTableFile) throws JSONException {
        jsonArray = new JSONArray(timeTableFile);
        timeTableList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length() ; i++)
        {
            JSONObject object = jsonArray.getJSONObject(i);
            timeTableList.add(new TimeTable(Integer.parseInt(object.getString("id")), object.getString("train_name"), object.getString("train_no"), object.getString("start_from"), LocalTime.parse(object.getString("time_arrival")),object.getString("to_destination"),LocalTime.parse(object.getString("time_departure"))));
        }
        return timeTableList;
    }

    @Test
    void TimeTable_testcase_1() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_1.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(),trainService.displayTimeTable().toString());

    }

    @Test
    void TimeTable_testcase_2() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_2.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(),trainService.displayTimeTableByYourCity("Lahore Junction").toString());

    }

    @Test
    void TimeTable_testcase_3() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_3.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(),trainService.displayTimeTableByYourCity("Multan Cantonment").toString());

    }

    @Test
    void TimeTable_testcase_4() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_4.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(),trainService.displayTimeTableByYourCity("noob city").toString());

    }
}
