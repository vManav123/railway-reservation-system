package railway.management.system.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import railway.management.system.ExceptionHandling.NoTrainExistException;
import railway.management.system.ExceptionHandling.TrainNotRunningOnThisDayException;
import railway.management.system.Models.*;
import railway.management.system.Repository.TrainRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    TrainRepository trainRepository;

    @Value("${get.city:Karachi City}")
    private String city;


    // Declared Useful Elements
    List<Train> trains;
    Train train;
    LocalDate localDate = LocalDate.now();
    List<TrainLocation> trainLocationList;
    List<TimeTable> timeTables;
    List<TrainsBetweenStation> trainsBetweenStations;




    // public Services
    @Override
    public List<Train> displayAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public String displayTrainToTable(Long trainNo) {
        train = displayTrain(trainNo);
        String Result = "------------------------------------------------------------------------------------------------------------------------------------------------------------\n"+
                        "                   Train No / Train Name : " + train.getTrain_no() + "/" + train.getTrain_name() + "                  "+train.getStart_from()+"  --------------->  "+train.getTo_destination()+"                   \n"+
                        "                   Run Days : "+train.getRun_days().toString()+"               Train Length : "+train.getTrain_length()+"                    "+train.getDeparture_time()+  "                          "+train.getArrival_time()+  "                         \n"+
                        "                   Coaches  : "+train.getCoaches_fair().keySet()+"            "+"Train Type : "+train.getTrain_type()+"\n"+
                        "------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n"+
                        "                                Station            |       Arrival       |        Departure\n------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
        for(Map.Entry<String,Detail> route : train.getRoute().entrySet())
        {
            Result+="                                ";
            String local="";
            local +=   route.getKey();
            int i = " Karachi Cantonment ".length()-local.length();
            Result+=addSpaces(local,i);
            Result +=   "      "+route.getValue().getArrival_time()+"                 "+route.getValue().getDeparture_time()+"\n";
        }
        return Result;
    }


    @Override
    public Train displayTrain(Long trainNo) {
        trains = trainRepository.findAll();
        return trains.stream().filter(p->p.getTrain_no().equals(trainNo.toString())).collect(Collectors.toList()).get(0);
    }

    @Override
    public String updateData(List<Train> trains) {


        trainRepository.saveAll(trains);
        return " !!!! All Data Updated Successfully !!!!";
    }

    @Override
    public String customUpdation() {
        List<Train> trainList = trainRepository.findAll();

        /*
         Add Custom Update Here
         */

        for(Train train : trainList)
        {
            // city , cantoment , Junction
            List<String> stationToBeDeleted;
            for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
            {
                if(train.getTrain_type().equals("Express"))
                {

                    int time = 0;
                    if(map.getKey().equals(train.getStart_from()) || map.getKey().equals(train.getTo_destination()))
                        continue;
                }
            }
        }

        trainRepository.saveAll(trainList);
        return "Data Updated Successfully";
    }


    // *----------------------  TimeTable Functionality  ------------------------------*

    @Override
    public List<TimeTable> displayTimeTable() {
        trains = displayAllTrains();
        timeTables = new ArrayList<>();
        int i = 0;
        trains.forEach( p -> {
            if(p.getRoute().containsKey(city)) {
                if(p.getRoute().get(city).getArrival_time().equals(p.getRoute().get(city).getDeparture_time()))
                {
                    p.getRoute().get(city).setArrival_time(p.getRoute().get(city).getDeparture_time().plusMinutes(5));
                }
                timeTables.add(new TimeTable(new Random().nextInt(1000), p.getTrain_name(), p.getTrain_no(), p.getStart_from(), p.getRoute().get(city).getArrival_time(), p.getTo_destination(), p.getRoute().get(city).getDeparture_time()));
            }
        });

        int x = 1;

        List<TimeTable> timeTable = timeTables.stream().sorted(Comparator.comparing(TimeTable :: getTime_arrival)).collect(Collectors.toList());
        for (TimeTable t : timeTable)
        {
            t.setId(x++);
        }
        return timeTable;
    }

    @Override
    public String displayTimeTableByYourCity(String city) {
        this.city = city;
        return displayTimeTableInTable(displayTimeTable());
    }

    public String displayTimeTableInTable(List<TimeTable> timeTables)
    {
        String result = "*------------------------------------------------  ***  Pakistan Rail Network Running Train TimeTable  ***  -------------------------------------------------*\n \n"
                      + "-------------------------------------------------------------------------------------------------------------------------------------------------------------- \n"
                      + "|  Train No  |               Train Name              |            Start              |           destination            |      arrival     |     departure    | \n"
                      + "-------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";

        for (TimeTable timeTable : timeTables)
        {
            result +=   "  "+timeTable.getTrain_no()+"          "+timeTable.getTrain_name();
            int i = "          Train Name            ".length()-timeTable.getTrain_name().length();
            result = addSpaces(result,i);
            result +="        "+timeTable.getStart_from();
            i = "            Start              ".length()-timeTable.getStart_from().length();
            result = addSpaces(result,i);
            result +=timeTable.getTo_destination();
            i = "           destination            ".length()-timeTable.getTo_destination().length();
            result = addSpaces(result,i);
            result += "   "+timeTable.getTime_arrival()+"           "+timeTable.getTime_departure()+"\n";
            result +=   "--------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        }
        return result;
    }

    // *--------------------------------------------------------------------------------------*


    // *--------------------- Trains Between Station Functionality ---------------------------*

    @Override
    public List<TrainsBetweenStation> trainsBetweenStation(String origin ,  String destination) {
        trainsBetweenStations = new ArrayList<>();
        trains = trainRepository.findAll();
        trains.forEach( trainData -> {
            if(trainData.getRoute().containsKey(origin) && trainData.getRoute().containsKey(destination))
            {
                Duration duration = Duration.between(trainData.getRoute().get(origin).getDeparture_time(), trainData.getRoute().get(destination).getArrival_time());
                String total_Time = Math.abs(duration.toHoursPart()) + "h " + Math.abs(duration.toMinutesPart()) + "m";
                HashMap<String,Double> map = trainData.getCoaches_fair().entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(p,q) -> p , HashMap::new));

                trainsBetweenStations.add(new TrainsBetweenStation(trainData.getTrain_no(),trainData.getTrain_name(),origin,trainData.getRoute().get(origin).getDeparture_time(),destination,trainData.getRoute().get(destination).getArrival_time(),total_Time,trainData.getRun_days(),map));
            }
        });
        return trainsBetweenStations;
    }
    public String addSpaces(String test , int length)
    {
        int i=length;
        while (--i>=0)
        {
            test+=" ";
        }
        return test;
    }

    public String trainsBetweenStationToTable(String origin , String destination)
    {
        List<TrainsBetweenStation> trainsBetweenStationList = trainsBetweenStation(origin,destination);
        String result = "*------------------------------------------------  ***  Trains Between "+trainsBetweenStationList.get(0).getOrigin()+"   --->    "+trainsBetweenStationList.get(0).getDestination()+"  ***  ---------------------------------------*\n \n"
                      + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n"
                      + "| Train No |         Train Name             |   Deprture   |    Arrival    |  Travel Time   |               Running Days                   |                Classes                          | \n"
                      + "---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";

        for(TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList) {

            String test = "| "+trainsBetweenStation.getTrain_no()+"  |    "+trainsBetweenStation.getTrain_name();
            int i = "| Train No |           Train Name           |".length()-test.length();
            test = addSpaces(test,i);
            test = test+"|";
            test +="   "+trainsBetweenStation.getArrival_time()+"   |"+"   "+trainsBetweenStation.getDeparture_time()+"    "+"|"+"   "+trainsBetweenStation.getTravel_time()+"      ";

            // +"          |          "+trainsBetweenStation.getClasses().keySet()
            String local ="|             "+trainsBetweenStation.getRun_days();
            i = "               Running Days                    ".length()-local.length();
            local = addSpaces(local,i)+"|";
            test+=local;
            local = "           "+trainsBetweenStation.getClasses().keySet();
            i="                     Classes                  ".length()-local.length();
            local = addSpaces(local,i);
            test+=local+"  |";
            result+=test+"\n"+"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        }
        return result;
    }


    // *-----------------------------------------------------------------------------------------*


    // *------------------------------- Train Location Functionality ----------------------------*
    public boolean isNumeric(String train_search_info)
    {
        try {
            double d = Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List<TrainLocation> trainLocation(String train_search_info,String day,String your_location) {

            trains = trainRepository.findAll();
            if (isNumeric(train_search_info)) {
                trains = trains.stream().filter(p -> p.getTrain_no().equals(train_search_info)).collect(Collectors.toList());
                try{
                    if (trains.isEmpty())
                        throw new NoTrainExistException("There is no train exist with this Train No");
                }
                catch (NoTrainExistException e)
                {
                    return Arrays.asList(new TrainLocation(e.toString(),LocalDateTime.now(),LocalDateTime.now(),"Not Reachable",12));
                }
                return getTrainRoute(train,day,your_location);
            }
            else {

                trains = trains.stream().filter(p -> p.getTrain_name().equals(train_search_info)).collect(Collectors.toList());
                try{
                    if (trains.isEmpty())
                        throw new NoTrainExistException("There is no train exist with this train name");
                }
                catch (NoTrainExistException e)
                {
                    return Arrays.asList(new TrainLocation(e.toString(),LocalDateTime.now(),LocalDateTime.now(),"Not Reachable",12));
                }
                return getTrainRoute(train,day,your_location);
            }
    }
    @Override
    public String trainLocationToTable(String train_search_info,String day,String your_location)
    {
        trainLocationList = trainLocation(train_search_info,day,your_location);
        train = trains.stream().filter(p -> p.getTrain_no().equals(train_search_info)).collect(Collectors.toList()).get(0);
        try{
            if(train==null)
                throw new NoTrainExistException("Ther is no train exist on this train Information");
        }
        catch (NoTrainExistException e)
        {
            return Arrays.stream(e.toString().split(":")).toList().get(1);
        }
        String train_location = "";
        TrainLocation previous_station = null;
        for(TrainLocation map : trainLocationList)
        {
            if(LocalDateTime.now().isAfter(map.getArrival_time()) && LocalDateTime.now().isBefore(map.getDeparture_time()))
            {
                train_location = map.getCurrent_station();
                break;
            }
            else if(previous_station!=null && LocalDateTime.now().isAfter(previous_station.getDeparture_time()) && LocalDateTime.now().isBefore(map.getArrival_time()))
            {
                train_location=previous_station.getCurrent_station();
                break;
            }
            previous_station = map;
        }

        LocalTime departure = LocalTime.now(),arrival = LocalTime.now();
        System.out.println(train_location);
        try {
            departure = train.getRoute().get(train_location).getDeparture_time();
            arrival = train.getRoute().get(your_location).getArrival_time();
        }
        catch (NullPointerException e)
        {

        }
        Duration duration = Duration.between(train.getArrival_time(),train.getDeparture_time());
        String total_Time = Math.abs(duration.toHoursPart()) + "h " + Math.abs(duration.toMinutesPart()) + "m";

            String result = "";
            result += "*-----------------------------------  Train No. : "+train.getTrain_no()+" , Train Name : "+train.getTrain_name()+"     ***  Live train Running Status  ***  -----------------------------------------*    \n \n"
                    + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n "
                    + "|           Station             |           Arrival           |               Departure              |              Train Status                |             Delay           | \n"
                    + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        String chk = "Not Started";
        for(TrainLocation trainLocation : trainLocationList)
        {

            if(!trainLocation.getCurrent_station().equals(train_location) && !chk.equals("Departed") && !chk.equals("Arriving"))
                chk = "Departed";
            else if(trainLocation.getCurrent_station().equals(train_location)) {
                chk = "Arriving";

                result += "    "+trainLocation.getCurrent_station()+"                  "+trainLocation.getArrival_time()+"                  "+trainLocation.getDeparture_time()+"                     "+trainLocation.getTrain_status()+" ( "+chk+" )"+"                      No Delay                  --->  !!!  Your Train is at this station , will Reach in "+total_Time+ "!!!\n"
                        + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
                continue;
            }
            else if(trainLocation.getCurrent_station().equals(your_location))
            {
                result += "    "+trainLocation.getCurrent_station()+"                  "+trainLocation.getArrival_time()+"                  "+trainLocation.getDeparture_time()+"                     "+trainLocation.getTrain_status()+" ( "+chk+" )"+"                      No Delay                  --->  !!!  Your are this Station !!!\n"
                        + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
                continue;
            }

            result += "    "+trainLocation.getCurrent_station()+"                   "+trainLocation.getArrival_time()+"                  "+trainLocation.getDeparture_time()+"                      "+trainLocation.getTrain_status()+" ( "+chk+" )"+"                       No Delay          \n"
                    + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        }
        return result;
    }
    public List<TrainLocation> getTrainRoute(Train train , String day , String your_location)
    {
        String previous_station = "null";

        if(day.isEmpty() || day.isBlank())
            day="today";
        train = trains.get(0);


        // *------------- NoTrainExistException handled -----------------*
        try{
            for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
                if (map.getValue().getArrival_time().isBefore(LocalTime.now()) && map.getValue().getDeparture_time().isAfter(LocalTime.now())) {
                    return getRoute(train, map.getKey(), "Arrived", day , your_location);
                } else if (!previous_station.equals("null") && LocalTime.now().isAfter(train.getRoute().get(previous_station).getDeparture_time()) && LocalTime.now().isBefore(map.getValue().getArrival_time())) {
                    return getRoute(train, previous_station, "departed", day , your_location);
                }
                previous_station = map.getKey();
            }
            throw new NoTrainExistException("There is no Train Exist on this information");
        }
        catch (NoTrainExistException e)
        {
            return Arrays.asList(new TrainLocation(e.toString(),LocalDateTime.MIN,LocalDateTime.MIN,"Not Reachable",0));
        }

    }
    public List<TrainLocation> getRoute(Train train,String train_location,String departed,String day , String your_location)
    {
        List<TrainLocation> route = new ArrayList<>();


        if(day.equalsIgnoreCase("yesterday"))
            localDate = localDate.minusDays(1);
        else if(day.equalsIgnoreCase("tomorrow"))
            localDate = localDate.plusDays(1);
        else if(day.contains("before") || day.contains("Before"))
            localDate = localDate.minusDays(Integer.parseInt(day.replaceAll("[^0-9]", "")));
        else if(day.contains("after") || day.contains("After"))
            localDate = localDate.plusDays(Integer.parseInt(day.replaceAll("[^0-9]", "")));


        // *------------- TrainNotRunningOnThisDayException handled -----------------*
        try{
            if(!train.getRun_days().contains(localDate.getDayOfWeek().toString().substring(0,3).toUpperCase())){
                throw new TrainNotRunningOnThisDayException("Train is not usually run on that day");
            }
        }
        catch (TrainNotRunningOnThisDayException e)
        {
            return Arrays.asList(new TrainLocation(e.toString(),LocalDateTime.now(),LocalDateTime.now(),"Not Reachable",12));
        }


        LocalDateTime departure = LocalDateTime.of(localDate,train.getDeparture_time());
        LocalDateTime arrival = departure;
        LocalDateTime Departure = departure;
        LocalTime previous_time = departure.toLocalTime();


        int chk = 0;
        for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
        {
            int platform;
            if(map.getKey().contains("Junction"))
                platform = new Random().nextInt(9);
            else
                platform = new Random().nextInt(4);

            departed = "Departed";
            if(map.getValue().getArrival_time().isAfter(LocalTime.NOON) && map.getValue().getDeparture_time().isBefore(LocalTime.NOON))
            {
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                localDate = localDate.plusDays(1);
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
                departed="Arriving";
                if(chk==0)
                    chk=2;
            }
            else if(previous_time.isAfter(LocalTime.NOON) && map.getValue().getArrival_time().isBefore(LocalTime.NOON))
            {
                localDate = localDate.plusDays(1);
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
                departed="Arriving";
                if(chk==0)
                    chk=1;
            }
            else
            {
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
            }
            previous_time = departure.toLocalTime();

            if(chk==1)
            {
                route.add(new TrainLocation(map.getKey(),arrival,Departure,"Departed",platform));
                chk=3;
                continue;
            }
            else if(chk==2)
            {
                route.add(new TrainLocation(map.getKey(),arrival,Departure,"Arrived",platform));
                chk=3;
                continue;
            }
            route.add(new TrainLocation(map.getKey(),arrival,Departure,departed,platform));
        }

        return route;
    }



    // *-------------------------------------------------------------------------------------------*

    // *------------------------------- train Fair Functionality ----------------------------------*

    @Override
    public List<TrainsBetweenStation> trainFair(String origin,String destination) {

        List<TrainsBetweenStation> trainsBetweenStationList = trainsBetweenStation(origin,destination);
        List<Train> trains = trainRepository.findAll();
        for(TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList)
        {
            Integer fare = 0;
            boolean chk = false;
            Train train = trains.stream().filter( p -> p.getTrain_name().equals(trainsBetweenStation.getTrain_name())).toList().get(0);
            String dest = destination;
            String start = origin;
            for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
            {
                if(map.getKey().equals(dest)) {
                    if(fare == 0)
                    {
                        String temp = dest;
                        dest = start;
                        start = temp;
                    }
                    else
                        break;
                }
                if(map.getKey().equals(start) || chk)
                {
                    chk = true;
                    fare+=map.getValue().getPrice();
                }
            }
            for (Map.Entry<String,Double> classes : trainsBetweenStation.getClasses().entrySet())
            {
                classes.setValue(classes.getValue()*fare);
            }
        }
        return trainsBetweenStationList;
    }
    @Override
    public String trainFairToTable(String origin, String destination) {
        List<TrainsBetweenStation> trainsBetweenStationList = trainFair(origin,destination);
        String result = "*-----------------------------------------------------------------------  ***  Trains Fare Between the Station  ***  -----------------------------------------------------------------------*\n \n"
                + "----------------------------------------------------- ######   " + trainsBetweenStationList.get(0).getOrigin() + "    --------->    "+trainsBetweenStationList.get(0).getDestination()+"   ##### ------------------------------------------------------------------------ \n"
                + "============================================================================================================================================================================================= \n \n \n";
        for(TrainsBetweenStation trainsBetweenStation : trainsBetweenStationList)
        {
            String local = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
            local += "|        Train No / Train Name   : "+trainsBetweenStation.getTrain_no() + " / " + trainsBetweenStation.getTrain_name()+"           Trip ===< "+trainsBetweenStation.getOrigin() + "  ----------- "+trainsBetweenStationList.get(0).getClasses().get("SEC")*0.4+" km | "+trainsBetweenStation.getTravel_time()+" --------->  "+trainsBetweenStation.getDestination()+" >===       Date : "+LocalDate.now()+"        \n";
            for(Map.Entry<String,Double> map : trainsBetweenStation.getClasses().entrySet())
            {
                local+= "|        "+map.getKey() + " : "+Math.round(map.getValue())+"        ";
            }
            local+="|\n"
                    +"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n\n";
            result+=local;
        }
        return result;
    }


    // *-------------------------------------------------------------------------------------------*


}
