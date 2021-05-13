package railway.reservation.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import railway.reservation.system.Models.TimeTable;
import railway.reservation.system.Models.TrainsBetweenStation;
import railway.reservation.system.Repository.TrainRepository;
import railway.reservation.system.Service.TrainService.TrainService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@TestComponent
public class Functionality_Testing {


    // *------------------------------------------ Autowired Services --------------------------------------------*

    @Autowired
    TrainService trainService;

    @Autowired
    TrainRepository trainRepository;


    // *----------------------------------------------------------------------------------------------------------*

    // *--------------------------------- TimeTable Functionality Testing ----------------------------------------*

    String timeTableFile;
    List<TimeTable> timeTableList;
    JSONArray jsonArray;
    String trainsBetweenStationFile;
    List<TrainsBetweenStation> trainBetweenStationList;

    List<TimeTable> getTimeTableList(String timeTableFile) throws JSONException {
        jsonArray = new JSONArray(timeTableFile);
        timeTableList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            timeTableList.add(new TimeTable(Integer.parseInt(object.getString("id")), object.getString("train_name"), object.getString("train_no"), object.getString("start_from"), object.getString("to_destination"), LocalTime.parse(object.getString("time_arrival")), LocalTime.parse(object.getString("time_departure"))));
        }
        return timeTableList;
    }

    @Test
    void TimeTable_testcase_1() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_1.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(), trainService.displayTimeTable("Karachi City").toString());

    }

    @Test
    void TimeTable_testcase_2() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_2.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(), trainService.displayTimeTable("Lahore Junction").toString());

    }

    // *-------------------------------------------------------------------------------------------------------------*

    // *-------------------------- TrainsBetweenStation Functionality Testing ---------------------------------------*

    @Test
    void TimeTable_testcase_3() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_3.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(), trainService.displayTimeTable("Multan Cantonment").toString());

    }

    @Test
    void TimeTable_testcase_4() throws IOException, JSONException {
        timeTableFile = Files.readString(Path.of("src/test/resources/TimeTable-TestCase/TimeTable_TestCase_4.json"));
        timeTableList = getTimeTableList(timeTableFile);
        Assertions.assertEquals(timeTableList.toString(), trainService.displayTimeTable("noob city").toString());

    }

    List<TrainsBetweenStation> getTrainsBetweenStationList(String TrainsBetweenStationFile) throws JSONException, JsonProcessingException {


        jsonArray = new JSONArray(TrainsBetweenStationFile);
        trainBetweenStationList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            ObjectMapper mapper = new ObjectMapper();
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Double.class);

            HashMap<String, Double> newmap = mapper.readValue(object.getString("classes"), mapType);
            HashMap<String, Double> map = newmap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (p, q) -> p, HashMap::new));

            List<String> list = mapper.readValue(object.getString("run_days"), List.class);

            trainBetweenStationList.add(new TrainsBetweenStation(object.getString("train_no"), object.getString("train_name"), object.getString("origin"), LocalTime.parse(object.getString("departure_time")), object.getString("destination"), LocalTime.parse(object.getString("arrival_time")), object.getString("travel_time"), list, map));
        }
        return trainBetweenStationList;
    }

    @Test
    public void TrainsBetweenStation_testcase_1() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainsBetweenStation-TestCase/TrainsBetweenStation-TestCase_1.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainsBetweenStation("Lahore Junction", "Karachi City").toString());

    }

    @Test
    void TrainsBetweenStation_testcase_2() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainsBetweenStation-TestCase/TrainsBetweenStation-TestCase_2.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainsBetweenStation("Lahore Junction", "Landhi Junction").toString());

    }

    @Test
    void TrainsBetweenStation_testcase_3() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainsBetweenStation-TestCase/TrainsBetweenStation-TestCase_3.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainsBetweenStation("", "").toString());

    }

    @Test
    void TrainsBetweenStation_testcase_4() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainsBetweenStation-TestCase/TrainsBetweenStation-TestCase_4.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainsBetweenStation("noob city", "Local City").toString());
    }

    // *------------------------------------------------------------------------------------------------------------------*


}
