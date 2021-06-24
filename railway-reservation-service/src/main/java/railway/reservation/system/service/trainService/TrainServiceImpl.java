package railway.reservation.system.service.trainService;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import railway.reservation.system.exceptions.InvalidTrainNoException;
import railway.reservation.system.exceptions.NoTrainExistException;
import railway.reservation.system.exceptions.StationNotExistException;
import railway.reservation.system.exceptions.TrainNotRunningOnThisDayException;
import railway.reservation.system.model.TimeTable;
import railway.reservation.system.model.TrainLocation;
import railway.reservation.system.model.TrainsBetweenStation;
import railway.reservation.system.model.train.Detail;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.repository.TrainRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrainServiceImpl implements TrainService {


    // *--------------------- Autowired Reference Variables----------------------*
    @Autowired
    TrainRepository trainRepository;
    // *-------------------------------------------------------------------------*




    // *---------------------------- Exception Messages -------------------------*
    String noTrainsExistException = " !!! There is no train exist on this train Information !!!";
    String stationNotExistException = "!!! there is no Station exist on this Train Route !!!";
    String trainNotRunningOnThisDayException = "!!! This train is not running usually on that day !!!";
    String invalidTrainNoException = "!!! Invalid Train Number !!! ";
    // *-------------------------------------------------------------------------*



    @Value("${get.city:Karachi City}")
    private String city;






    // *--------------------------- Basic Functionalities -----------------------*
    @Override
    public List<Train> displayAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train displayTrain(String trainNo) {
        List<Train> trains = trainRepository.findAll();
        try {
            if (trains.parallelStream().filter(p -> p.getTrain_no().equals(trainNo)).collect(Collectors.toList()).isEmpty())
                throw new NoTrainExistException(trainNotRunningOnThisDayException);
        } catch (NoTrainExistException e) {
            return new Train(e.getMessage());
        }
        return trains.parallelStream().filter(p -> p.getTrain_no().equals(trainNo.toString())).collect(Collectors.toList()).get(0);
    }

    @Override
    public String updateData(List<Train> trains) {
        trainRepository.saveAll(trains);
        return " !!!! All Data Updated Successfully !!!!";
    }

    @Override
    public String customUpdation() {
        return "Data Updated Successfully";
    }

    @Override
    public Train getTrainByTrainNo(String train_no) {
        return trainRepository.findAll().parallelStream().filter(p -> p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0);
    }

    @Override
    public boolean trainExistByTrainNo(String train_no) {
        return !trainRepository.findAll().parallelStream().filter(p -> p.getTrain_no().equals(train_no)).collect(Collectors.toList()).isEmpty();
    }

    @Override
    public String deleteAllTrains(String confirmation) {
        if(confirmation.equalsIgnoreCase("Yes"))
            return "Deletion Unsuccessful";
        log.info("All Train Data is Deleting");
        trainRepository.deleteAll();
        log.info("All Train Data is deleted Successfully");
        return "All Train Data is deleted Successfully";
    }

    @Override
    public String deleteTrain(String train_no,String confirmation) {

        if(confirmation.equalsIgnoreCase("Yes"))
            return "Deletion Unsuccessful";

        try {
            if(trainRepository.findAll().parallelStream().noneMatch(p -> train_no.equalsIgnoreCase(p.getTrain_no())))
                throw new InvalidTrainNoException(invalidTrainNoException);
        }
        catch (InvalidTrainNoException e)
        {
            return e.getMessage();
        }
        log.info("Train Data is Deleting");
        trainRepository.delete(trainRepository.findAll().parallelStream().filter(p->p.getTrain_no().equals(train_no)).collect(Collectors.toList()).get(0));
        log.info("Train Data is deleted Successfully");
        return "Train Data is deleted Successfully";
    }

    @Override
    public String addTrain(Train train) {
        try{
            if(isNumeric(train.getTrain_no()))
                throw new InvalidTrainNoException(invalidTrainNoException);
        }
        catch (InvalidTrainNoException e)
        {
            return e.getMessage();
        }
        trainRepository.save(train);
        log.info("All Train Data is deleted Successfully");
        return "All Train Data is deleted Successfully";
    }
    // *--------------------------------------------------------------------------*


    // *------------------------Train Details Functionality ----------------------*
    @Override
    public String displayTrainToTable(String trainNo) {

        Train train = displayTrain(trainNo);
        try {
            if (train.getRun_days() == null) {
                throw new NoTrainExistException(trainNotRunningOnThisDayException + " " + trainNo);
            }
        } catch (NoTrainExistException e) {
            return e.getMessage();
        }
        StringBuilder result = new StringBuilder("------------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                "                   Train No / Train Name : " + train.getTrain_no() + "/" + train.getTrain_name() + "                               " + train.getStart_from() + "  --------------->  " + train.getTo_destination() + "                   \n" +
                "                   Run Days : " + train.getRun_days().toString() + "               Train Length : " + train.getTrain_length() + "                    " + train.getDeparture_time() + "                          " + train.getArrival_time() + "                         \n" +
                "                   Coaches  : " + train.getCoaches_fair().keySet() + "            " + "Train Type : " + train.getTrain_type() + "\n" +
                "------------------------------------------------------------------------------------------------------------------------------------------------------------\n" +
                "                                Station               |       Arrival       |         Departure\n------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        for (Map.Entry<String, Detail> route : train.getRoute().entrySet()) {
            result.append("                                 ");
            String local = "";
            local += route.getKey();
            int i = "  Karachi Cantonment   ".length() - local.length();
            result.append(addSpaces(local, i));
            result.append("      ").append(route.getValue().getArrival_time()).append("                 ").append(route.getValue().getDeparture_time()).append("\n");
        }
        return result.toString();
    }
    // *-------------------------------------------------------------------------------*


    // *-------------------------  TimeTable Functionality  ---------------------------*

    @Override
    public List<TimeTable> displayTimeTable(String City) {
        if (City.length() != 0) {
            city = City;
        }

        List<Train> trains = displayAllTrains();
        List<TimeTable> timeTables = new ArrayList<>();

        trains.forEach(p -> {
            if (p.getRoute().containsKey(city)) {
                if (p.getRoute().get(city).getArrival_time().equals(p.getRoute().get(city).getDeparture_time())) {
                    p.getRoute().get(city).setArrival_time(p.getRoute().get(city).getDeparture_time().plusMinutes(5));
                }
                timeTables.add(new TimeTable(new Random().nextInt(1000), p.getTrain_name(), p.getTrain_no(), p.getStart_from(), p.getTo_destination(), p.getRoute().get(city).getArrival_time(), p.getRoute().get(city).getDeparture_time()));
            }
        });

        int x = 1;

        List<TimeTable> timeTable = timeTables.parallelStream().sorted(Comparator.comparing(TimeTable::getTime_arrival)).collect(Collectors.toList());

        for (TimeTable t : timeTable) {
            t.setId(x++);
        }
        return timeTable;
    }

    @Override
    public String displayTimeTableByYourCity(String city) {
        this.city = city;
        return displayTimeTableInTable(displayTimeTable(WordUtils.capitalizeFully(city)));
    }

    public String displayTimeTableInTable(List<TimeTable> timeTables) {
        String result = """
                *------------------------------------------------  ***  Pakistan Rail Network Running Train TimeTable  ***  -------------------------------------------------*
                \s
                --------------------------------------------------------------------------------------------------------------------------------------------------------------\s
                |  Train No  |               Train Name              |            Start              |           destination            |      arrival     |     departure    |\s
                --------------------------------------------------------------------------------------------------------------------------------------------------------------\s
                """;

        if (timeTables.isEmpty())
            return "There is no Station Exist on the Route Pakistan Rail Network";

        for (TimeTable timeTable : timeTables) {
            result += "  " + timeTable.getTrain_no() + "          " + timeTable.getTrain_name();
            int i = "          Train Name            ".length() - timeTable.getTrain_name().length();
            result = addSpaces(result, i);
            result += "        " + timeTable.getStart_from();
            i = "            Start              ".length() - timeTable.getStart_from().length();
            result = addSpaces(result, i);
            result += timeTable.getTo_destination();
            i = "           destination            ".length() - timeTable.getTo_destination().length();
            result = addSpaces(result, i);
            result += "   " + timeTable.getTime_arrival() + "           " + timeTable.getTime_departure() + "\n";
            result += "--------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        }
        return result;
    }

    // *--------------------------------------------------------------------------------------*


    // *--------------------- Trains Between Station Functionality ---------------------------*

    @Override
    public List<TrainsBetweenStation> trainsBetweenStation(String origin, String destination) {

        List<TrainsBetweenStation> trainsBetweenStations = new ArrayList<>();
        List<Train> trains = trainRepository.findAll();

        trains.forEach(trainData -> {
            if (trainData.getRoute().containsKey(WordUtils.capitalizeFully(origin)) && trainData.getRoute().containsKey(WordUtils.capitalizeFully(destination))) {

                Duration duration = Duration.between(trainData.getRoute().get(WordUtils.capitalizeFully(origin)).getDeparture_time(), trainData.getRoute().get(WordUtils.capitalizeFully(destination)).getArrival_time());
                String total_Time = Math.abs(duration.toHoursPart()) + "h " + Math.abs(duration.toMinutesPart()) + "m";
                HashMap<String, Double> map = trainData.getCoaches_fair().entrySet().parallelStream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (p, q) -> p, HashMap::new));

                trainsBetweenStations.add(new TrainsBetweenStation(trainData.getTrain_no(), trainData.getTrain_name(), WordUtils.capitalizeFully(origin), trainData.getRoute().get(WordUtils.capitalizeFully(origin)).getDeparture_time(), WordUtils.capitalizeFully(destination), trainData.getRoute().get(WordUtils.capitalizeFully(destination)).getArrival_time(), total_Time, trainData.getRun_days(), map));
            }
        });
        return trainsBetweenStations;
    }

    public String addSpaces(String test, int length) {
        int i = length;
        StringBuilder testBuilder = new StringBuilder(test);
        while (--i >= 0) {
            testBuilder.append(" ");
        }
        test = testBuilder.toString();
        return test;
    }

    public String trainsBetweenStationToTable(String origin, String destination) {
        List<TrainsBetweenStation> trainsBetweenStationList = trainsBetweenStation(origin, destination);
        try {
            if (trainsBetweenStationList.isEmpty())
                throw new TrainNotRunningOnThisDayException("There is Mistake in the name of Origin or Destination");
        } catch (IndexOutOfBoundsException | TrainNotRunningOnThisDayException e) {
            return e.getMessage();
        }
        StringBuilder result = new StringBuilder("*------------------------------------------------  ***  Trains Between " + trainsBetweenStationList.get(0).getOrigin() + "   --->    " + trainsBetweenStationList.get(0).getDestination() + "  ***  ---------------------------------------*\n \n"
                + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n"
                + "| Train No |         Train Name             |   Departure   |    Arrival    |  Travel Time   |               Running Days                   |                Classes                          | \n"
                + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n");

        for (TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList) {

            String test = "| " + trainsBetweenStation.getTrain_no() + "  |    " + trainsBetweenStation.getTrain_name();
            int i = "| Train No |           Train Name           |".length() - test.length();
            test = addSpaces(test, i);
            test = test + "|";
            test += "   " + trainsBetweenStation.getArrival_time() + "   |" + "   " + trainsBetweenStation.getDeparture_time() + "    " + "|" + "   " + trainsBetweenStation.getTravel_time() + "      ";

            // +"          |          "+trainsBetweenStation.getClasses().keySet()
            String local = "|             " + trainsBetweenStation.getRun_days();
            i = "               Running Days                    ".length() - local.length();
            local = addSpaces(local, i) + "|";
            test += local;
            local = "           " + trainsBetweenStation.getClasses().keySet();
            i = "                     Classes                  ".length() - local.length();
            local = addSpaces(local, i);
            test += local + "  |";
            result.append(test).append("\n").append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n");
        }
        return result.toString();
    }


    // *-----------------------------------------------------------------------------------------*


    // *------------------------------- Train Location Functionality ----------------------------*
    public boolean isNumeric(String train_search_info) {
        try {
            Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List<TrainLocation> trainLocation(String train_search_info, LocalDate date, String your_location) {

        List<Train> trains = trainRepository.findAll();
        trains = trains.parallelStream().filter(p -> p.getTrain_no().equals(train_search_info)).collect(Collectors.toList());
            try {
                if (trains.isEmpty())
                    throw new NoTrainExistException(noTrainsExistException + " " + train_search_info);
            } catch (NoTrainExistException e) {
                return Collections.singletonList(new TrainLocation(e.getMessage(), LocalDateTime.now(), LocalDateTime.now(), "Not Reachable", -1));
            }
        return getTrainRoute(trains.get(0), date, your_location);
    }

    public List<TrainLocation> getTrainRoute(Train train, LocalDate date, String your_location) {
        String previous_station = "null";


        // *------------- NoTrainExistException handled -----------------*
        try {
            for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
                if (map.getValue().getArrival_time().isBefore(LocalTime.now()) && map.getValue().getDeparture_time().isAfter(LocalTime.now())) {
                    return getRoute(train, map.getKey(), "Arrived", date, your_location);
                } else if (!previous_station.equals("null") && LocalTime.now().isAfter(train.getRoute().get(previous_station).getDeparture_time()) && LocalTime.now().isBefore(map.getValue().getArrival_time())) {
                    return getRoute(train, previous_station, "departed", date, your_location);
                }
                previous_station = map.getKey();
            }
            throw new NoTrainExistException(noTrainsExistException);
        } catch (NoTrainExistException e) {
            return Collections.singletonList(new TrainLocation(e.getMessage(), LocalDateTime.now(), LocalDateTime.now(), "Not Reachable", -1));
        }

    }

    public List<TrainLocation> getRoute(Train train, String train_location, String departed, LocalDate date, String your_location) {
        List<TrainLocation> route = new ArrayList<>();
        LocalDate localDate = date;

        // *------------- TrainNotRunningOnThisDayException handled -----------------*
        try {
            if (!train.getRun_days().contains(localDate.getDayOfWeek().toString().substring(0, 3).toUpperCase())) {
                throw new TrainNotRunningOnThisDayException(trainNotRunningOnThisDayException);
            }
        } catch (TrainNotRunningOnThisDayException e) {
            return Collections.singletonList(new TrainLocation(e.getMessage(), LocalDateTime.now(), LocalDateTime.now(), "Not Reachable", -1));
        }


        LocalDateTime arrival = LocalDateTime.of(localDate, train.getDeparture_time());
        LocalDateTime Departure = LocalDateTime.of(localDate, train.getDeparture_time());
        LocalTime previous_time = train.getDeparture_time();


        int chk = 0;
        for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
            int platform;
            if (map.getKey().contains("Junction"))
                platform = new Random().nextInt(9);
            else
                platform = new Random().nextInt(4);

            if ((map.getValue().getArrival_time().isAfter(LocalTime.NOON) && map.getValue().getDeparture_time().isBefore(LocalTime.NOON))) {
                arrival = LocalDateTime.of(localDate, map.getValue().getArrival_time());
                localDate = localDate.plusDays(1);
                Departure = LocalDateTime.of(localDate, map.getValue().getDeparture_time());
                chk = 2;
            } else if ( (previous_time.isAfter(LocalTime.NOON) && map.getValue().getArrival_time().isBefore(LocalTime.NOON))) {
                localDate = localDate.plusDays(1);
                arrival = LocalDateTime.of(localDate, map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate, map.getValue().getDeparture_time());
                chk = 1;
            } else {
                arrival = LocalDateTime.of(localDate, map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate, map.getValue().getDeparture_time());
            }
            previous_time = Departure.toLocalTime();

            if (chk == 1) {
                route.add(new TrainLocation(map.getKey(), arrival, Departure, "Departed", platform));
                chk = 3;
            } else if (chk == 2) {
                route.add(new TrainLocation(map.getKey(), arrival, Departure, "Departed", platform));
                chk = 3;
            }
            else if(LocalDateTime.now().isBefore(LocalDateTime.of(LocalDate.now(),map.getValue().getDeparture_time())))
                route.add(new TrainLocation(map.getKey(), arrival, Departure, "Departed", platform));
            else if(LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(),map.getValue().getDeparture_time())))
                route.add(new TrainLocation(map.getKey(), arrival, Departure, "Departed", platform));
        }
        return route;
    }


    // *------------------------------------- Train Location List to Table --------------------------------------*
    @Override
    public String trainLocationToTable(String train_search_info, LocalDate date, String your_location) {

        List<TrainLocation> trainLocationList = trainLocation(train_search_info, date,WordUtils.capitalizeFully(your_location));
        if(trainLocationList.size()==1 && trainLocationList.get(0).getPlatform()==-1)
            return trainLocationList.get(0).getCurrent_station();


        Train train = trainRepository
                    .findAll()
                    .parallelStream()
                    .filter(p -> p.getTrain_no().equals(train_search_info))
                    .collect(Collectors.toList())
                    .get(0);


        //  Your Location Tested
        try
        {
            if(!train.getRoute().containsKey(WordUtils.capitalizeFully(your_location)))
                throw new StationNotExistException(stationNotExistException);
        }
        catch (StationNotExistException e)
        {
            return e.getMessage();
        }
        log.info("Your Location Found int the Route");


        String status = trainLocationList.get(0).getDeparture_time().isAfter(LocalDateTime.of(date,LocalTime.now())) ? "Arriving" : "Departed";
        for (TrainLocation trainLocation : trainLocationList)
        {
            if(LocalDateTime.of(date,LocalTime.now()).isAfter(trainLocation.getArrival_time()) && LocalDateTime.of(date,LocalTime.now()).isBefore(trainLocation.getDeparture_time()))
                status="Arrived";
            else if(LocalDateTime.of(date,LocalTime.now()).isBefore(trainLocation.getArrival_time()))
                status="Arriving";
            else if(LocalDateTime.of(date,LocalTime.now()).isAfter(trainLocation.getDeparture_time()))
                status="Departed";
            trainLocation.setTrain_status(status);
        }
        String s = WordUtils.capitalizeFully(your_location);
        String train_location = trainLocationList.parallelStream().filter(p-> p.getTrain_status().equals("Arrived") || p.getTrain_status().equals("Arriving")).collect(Collectors.toList()).get(0).getCurrent_station();
        List<TrainLocation> trainLocations = trainLocationList
                .parallelStream()
                .filter(p->WordUtils.capitalizeFully(train_location).equalsIgnoreCase(p.getCurrent_station()) || WordUtils.capitalizeFully(your_location).equalsIgnoreCase(p.getCurrent_station()))
                .collect(Collectors.toList());

        Duration duration = Duration.between(trainLocations.get(0).getArrival_time()
                                            ,
                                            trainLocations
                                                    .parallelStream()
                                                    .filter(p->p.getCurrent_station().equals(s))
                                                    .collect(Collectors.toList())
                                                    .get(0)
                                                    .getArrival_time());


        StringBuilder result = new StringBuilder();
        result.append("*-----------------------------------  Train No. : ").append(train.getTrain_no()).append(" , Train Name : ").append(train.getTrain_name()).append("     ***  Live train Running Status  ***  -----------------------------------------*    \n \n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n ").append("|           Station             |           Arrival           |            Departure           |          Train Status          |     Delay     | \n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n");
        String chk = "Not Started";
        boolean test = true;
        boolean test2 = false;
        for (TrainLocation trainLocation : trainLocationList) {
            chk=trainLocation.getTrain_status();

            String local = "    "+trainLocation.getCurrent_station();
            result.append(addSpaces(local,"|           Station             ".length()-local.length()));

            if(chk.equals("Arrived") || (test && chk.equals("Arriving")))
            {
                if(!test2) {
                    result.append("       ").append(trainLocation.getArrival_time()).append("              ").append(trainLocation.getDeparture_time()).append("           ").append(chk).append(" ( ").append("Ontime").append(" )").append("            No Delay    |      -----> Your Train at this Location , it reach your location in "+duration.toHoursPart()+" h "+duration.toMinutesPart()+" m "+"\n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n");
                }else
                {
                    result.append("       ").append(trainLocation.getArrival_time()).append("              ").append(trainLocation.getDeparture_time()).append("           ").append(chk).append(" ( ").append("Ontime").append(" )").append("            No Delay    |      -----> Your Train at this Location , it passed from your location\n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n");
                }
                test=false;
                continue;
            }
            else if(trainLocation.getCurrent_station().equals(your_location))
            {
                test2=true;
                    result.append("      ").append(trainLocation.getArrival_time()).append("                ").append(trainLocation.getDeparture_time()).append("           ").append(chk).append(" ( ").append("Ontime").append(" )").append("           No Delay    |      -----> Your are at this station\n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n");
                continue;
            }
                    result.append("       ").append(trainLocation.getArrival_time()).append("              ").append(trainLocation.getDeparture_time()).append("           ").append(chk).append(" ( ").append("Ontime").append(" )").append("            No Delay    |\n").append("-------------------------------------------------------------------------------------------------------------------------------------------------- \n");
        }
        return result.toString();
    }

    // *-------------------------------------------------------------------------------------------*

    // *------------------------------- train Fair Functionality ----------------------------------*

    @Override
    public List<TrainsBetweenStation> trainFair(String origin, String destination) {

        List<TrainsBetweenStation> trainsBetweenStationList = trainsBetweenStation(origin, destination);
        List<Train> trains = trainRepository.findAll();
        for (TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList) {
            Integer fare = 0;
            boolean chk = false;
            Train train = trains.parallelStream().filter(p -> p.getTrain_name().equals(trainsBetweenStation.getTrain_name())).collect(Collectors.toList()).get(0);
            String dest = destination;
            String start = origin;
            for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
                if (map.getKey().equals(dest)) {
                    if (fare == 0) {
                        String temp = dest;
                        dest = start;
                        start = temp;
                    } else
                        break;
                }
                if (map.getKey().equals(start) || chk) {
                    chk = true;
                    fare += map.getValue().getPrice();
                }
            }
            for (Map.Entry<String, Double> classes : trainsBetweenStation.getClasses().entrySet()) {
                classes.setValue(classes.getValue() * fare);
            }
        }
        return trainsBetweenStationList;
    }

    @Override
    public String trainFairToTable(String origin, String destination) {
        List<TrainsBetweenStation> trainsBetweenStationList;
        try {
            trainsBetweenStationList = trainFair(origin, destination);
            trainsBetweenStationList.get(0).toString();
        } catch (IndexOutOfBoundsException e) {
            return "There is No City Exist with these names";
        }
        StringBuilder result = new StringBuilder("*-----------------------------------------------------------------------  ***  Trains Fare Between the Station  ***  -----------------------------------------------------------------------*\n \n"
                + "--------------------------------------------------------------- ######   " + trainsBetweenStationList.get(0).getOrigin() + "    --------->    " + trainsBetweenStationList.get(0).getDestination() + "   ##### ------------------------------------------------------------- \n"
                + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ \n \n \n");
        for (TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList) {
            StringBuilder local = new StringBuilder("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n");
            local.append("|        Train No / Train Name   : ").append(trainsBetweenStation.getTrain_no()).append(" / ").append(trainsBetweenStation.getTrain_name()).append("           Trip ===< ").append(trainsBetweenStation.getOrigin()).append("  ----------- ").append(trainsBetweenStationList.get(0).getClasses().get("SEC") * 0.4).append(" km | ").append(trainsBetweenStation.getTravel_time()).append(" --------->  ").append(trainsBetweenStation.getDestination()).append(" >===       Date : ").append(LocalDate.now()).append("        \n");
            for (Map.Entry<String, Double> map : trainsBetweenStation.getClasses().entrySet()) {
                local.append("|        ").append(map.getKey()).append(" : ").append(Math.round(map.getValue())).append("        ");
            }
            local.append("""
                    |
                    ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


                    """);
            result.append(local);
        }
        return result.toString();
    }


    // *-------------------------------------------------------------------------------------------*


}
