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
import org.springframework.data.mongodb.repository.MongoRepository;
import railway.reservation.system.model.TimeTable;
import railway.reservation.system.model.TrainLocation;
import railway.reservation.system.model.TrainsBetweenStation;
import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.repository.ReservedTicketRepository;
import railway.reservation.system.service.reservationService.ReservationService;
import railway.reservation.system.service.trainService.TrainService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@TestComponent
public class Functionality_Testing {


    // *------------------------------------------ Autowired Services --------------------------------------------*

    @Autowired
    private TrainService trainService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MongoRepository<Train, Long> mongoRepository;
    @Autowired
    private ReservedTicketRepository reservedTicketRepository;
    // *----------------------------------------------------------------------------------------------------------*




    // *--------------------------------- TimeTable Functionality Testing ----------------------------------------*

    String timeTableFile;
    List<TimeTable> timeTableList;
    JSONArray jsonArray;
    String trainsBetweenStationFile;
    List<TrainsBetweenStation> trainBetweenStationList;
    String trainLocationFile;
    List<TrainLocation> trainLocationList;

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
    

    // *-------------------------------------------------------------------------------------------------------------*

    // *-------------------------- TrainsBetweenStation Functionality Testing ---------------------------------------*

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



    // *---------------------------------------------------------- Train Fair ---------------------------------------------*
    @Test
    public void TrainsFare_testcase_1() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainFare-TestCase/TrainFare_TestCase_1.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainFair("Lahore Junction", "Karachi City").toString());

    }

    @Test
    void TrainsFare_testcase_2() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainFare-TestCase/TrainFare_TestCase_2.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainFair("Lahore Junction", "Landhi Junction").toString());

    }

    @Test
    void TrainsFare_testcase_3() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainFare-TestCase/TrainFare_TestCase_3.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainFair("", "").toString());

    }

    @Test
    void TrainsFare_testcase_4() throws IOException, JSONException {
        trainsBetweenStationFile = Files.readString(Path.of("src/test/resources/TrainFare-TestCase/TrainFare_TestCase_4.json"));
        trainBetweenStationList = getTrainsBetweenStationList(trainsBetweenStationFile);
        Assertions.assertEquals(trainBetweenStationList.toString(), trainService.trainFair("noob city", "Local City").toString());
    }
    // *-------------------------------------------------------------------------------------------------------------------*




    // *--------------------------------------------------- Display Train ------------------------------------------------*
    @Test
    void train_testcase_1() throws IOException, JSONException {
        String train_no = "410878";
        Train train = mongoRepository.findAll().stream().filter(p->p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void train_testcase_2() throws IOException, JSONException {
        String train_no = "5555108";
        Train train = new Train(null,"This train is not running usually on that day",null,null,null,null,null,null, new Hashtable<>(),0,false,null);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void train_testcase_3() throws IOException, JSONException {
        String train_no = "778120";
        Train train = mongoRepository.findAll().stream().filter(p->p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void train_testcase_4() throws IOException, JSONException {
        String train_no = "4108";
        Train train = new Train(null,"This train is not running usually on that day",null,null,null,null,null,null, new Hashtable<>(),0,false,null);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    // *-------------------------------------------------------------------------------------------------------------------*


    // *----------------------------------------------- Get Ticket --------------------------------------------------------*
    @Test
    void pnr_testcase_1() throws IOException, JSONException {
        String train_no = "410878";
        Train train = mongoRepository.findAll().stream().filter(p->p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void pnr_testcase_2() throws IOException, JSONException {
        String train_no = "5555108";
        Train train = new Train(null,"This train is not running usually on that day",null,null,null,null,null,null, new Hashtable<>(),0,false,null);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void pnr_testcase_3() throws IOException, JSONException {
        String train_no = "778120";
        Train train = mongoRepository.findAll().stream().filter(p->p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    @Test
    void pnr_testcase_4() throws IOException, JSONException {
        String train_no = "4108";
        Train train = new Train(null,"This train is not running usually on that day",null,null,null,null,null,null, new Hashtable<>(),0,false,null);
        Assertions.assertEquals(train.toString(), trainService.displayTrain(train_no).toString());
    }
    // *-------------------------------------------------------------------------------------------------------------------*



    // *----------------------------------------------- Ticket cancellation -----------------------------------------------*
    @Test
    public void ticketCancellation_TestCase_1()
    {
        long pnr = 1231112L;
        ReservedTicket reservedTicket = new ReservedTicket();
        reservedTicket.setPnr(pnr);
        reservedTicketRepository.save(reservedTicket);
        Assertions.assertEquals("success",reservationService.ticketCancellation(pnr));
    }
    @Test
    public void ticketCancellation_TestCase_2()
    {
        long pnr = 12312312L;
        ReservedTicket reservedTicket = new ReservedTicket();
        reservedTicket.setPnr(pnr);
        reservedTicketRepository.save(reservedTicket);
        Assertions.assertEquals("success",reservationService.ticketCancellation(pnr));
    }
    @Test
    public void ticketCancellation_TestCase_3()
    {
        long pnr = 12312312L;
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",reservationService.ticketCancellation(pnr));
    }
    @Test
    public void ticketCancellation_TestCase_4()
    {
        long pnr = 12312312L;
        Assertions.assertEquals("!!! Invalid PNR !!! , Please write Information correctly.",reservationService.ticketCancellation(pnr));
    }
    // *--------------------------------------------------------------------------------------------------------------------*

}
