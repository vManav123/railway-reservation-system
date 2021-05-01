package railway.management.system.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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


    // Models
    List<Train> trains;
    Train train;
    LocalDate localDate = LocalDate.now();




    // public Services
    @Override
    public List<Train> displayAllTrains() {
        return trainRepository.findAll();
    }


    @Override
    public Train displayTrain(Long trainNo) {
        trains = trainRepository.findAll();
        return trains.stream().filter(p->p.getTrain_no().equals(trainNo.toString())).collect(Collectors.toList()).get(0);
    }

    @Override
    public String updateData() {
        trains = trainRepository.findAll();
        for(Train train : trains)
        {
            LocalTime time = train.getDeparture_time();
            String start = train.getStart_from();
            for(Map.Entry<String,Detail> route : train.getRoute().entrySet())
            {
                if(route.getKey().equals(start))
                    continue;
                route.getValue().setArrival_time(time.plusMinutes(new Random().nextInt(8)+10).plusSeconds(new Random().nextInt(60)));
                route.getValue().setDeparture_time(route.getValue().getArrival_time().plusMinutes(new Random().nextInt(3)+2));
                time=route.getValue().getDeparture_time();
            }
        }
        trainRepository.saveAll(trains);
        return " !!!! All Data Updated Successfully !!!!";
    }


    // *----------------------  TimeTable Functionality  ------------------------------*

    @Override
    public List<TimeTable> displayTimeTable() {
        List<Train> trains = displayAllTrains();
        List<TimeTable> timeTables = new ArrayList<>();
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
    public List<TimeTable> displayTimeTableByYourCity(String city) {
        this.city = city;
        return displayTimeTable();
    }

    // *--------------------------------------------------------------------------------------*


    // *--------------------- Trains Between Station Functionality ---------------------------*

    @Override
    public List<TrainsBetweenStation> trains_between_station(String origin ,  String destination) {
        List<TrainsBetweenStation> trainsBetweenStations = new ArrayList<>();
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

    // *-----------------------------------------------------------------------------------------*


    // *------------------------------- Train Location Functionality ----------------------------*

    @Override
    public String trainLocation(String train_search_info,String day,String your_location) {

            if (isNumeric(train_search_info)) {
                trains = trainRepository.findAll();
                trains = trains.stream().filter(p -> p.getTrain_no().equals(train_search_info)).collect(Collectors.toList());
                if (trains.isEmpty())
                    return "No such trains No Exit";
                return getTrainRoute(train,day,your_location);
            }
            else {
                trains = trainRepository.findAll();
                trains = trains.stream().filter(p -> p.getTrain_name().equals(train_search_info)).collect(Collectors.toList());
                if (trains.isEmpty())
                    return "No such trains Name Exit";
                return getTrainRoute(train,day,your_location);
            }
    }
    public String getTrainRoute(Train train , String day , String your_location)
    {
        String previous_station = "null";

        if(day.isEmpty())
            day="today";
        train = trains.get(0);

        for (Map.Entry<String, Detail> map : train.getRoute().entrySet()) {
            if (map.getValue().getArrival_time().isBefore(LocalTime.now()) && map.getValue().getDeparture_time().isAfter(LocalTime.now())) {
                return getRoute(train, map.getKey(), "null", day , your_location);
            } else if (!previous_station.equals("null") && LocalTime.now().isAfter(train.getRoute().get(previous_station).getDeparture_time()) && LocalTime.now().isBefore(map.getValue().getArrival_time())) {
                return getRoute(train, previous_station, "departed", day , your_location);
            }
            previous_station = map.getKey();
        }
        return "No train Exist";
    }

    public boolean isNumeric(String train_search_info)
    {
        try {
            double d = Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String getRoute(Train train,String train_location,String departed,String day , String your_location)
    {
        List<TrainLocation> route = new ArrayList<>();


        if(day.equalsIgnoreCase("Before2days"))
            localDate = localDate.minusDays(2);
        else if(day.equalsIgnoreCase("yesterday"))
            localDate = localDate.minusDays(1);
        else if(day.equalsIgnoreCase("tomorrow"))
            localDate = localDate.plusDays(1);
        else if(day.equalsIgnoreCase("after2days"))
            localDate = localDate.plusDays(2);





        LocalDateTime departure = LocalDateTime.of(localDate,train.getDeparture_time());
        LocalDateTime arrival = departure;
        LocalDateTime Departure = departure;
        LocalTime previous_time = departure.toLocalTime();
        for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
        {
            int platform;
            if(map.getKey().contains("Junction"))
                platform = 3;
            else
                platform = 2;


            if(map.getValue().getArrival_time().isAfter(LocalTime.NOON) && map.getValue().getDeparture_time().isBefore(LocalTime.NOON))
            {
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                localDate = localDate.plusDays(1);
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
            }
            else if(previous_time.isAfter(LocalTime.NOON) && map.getValue().getArrival_time().isBefore(LocalTime.NOON))
            {
                localDate = localDate.plusDays(1);
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
            }
            else
            {
                arrival = LocalDateTime.of(localDate,map.getValue().getArrival_time());
                Departure = LocalDateTime.of(localDate,map.getValue().getDeparture_time());
            }
            previous_time = departure.toLocalTime();

            route.add(new TrainLocation(map.getKey(),arrival,Departure,"Ontime",platform));
        }


        /* Train Location and Station */
        Duration duration = Duration.between(train.getRoute().get(train_location).getDeparture_time(), train.getRoute().get(your_location).getArrival_time());
        String total_Time = Math.abs(duration.toHoursPart()) + "h " + Math.abs(duration.toMinutesPart()) + "m";

        TrainLocation previous_station = null;
        for(TrainLocation map : route)
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


        /*  Result Showing on the Api */

        String result = "";
        if(route.isEmpty())
            return result+"  !!! There is no Train on this Train information !!!  ";
        else
            result += "*-----------------------------------  Train No. : "+train.getTrain_no()+" , Train Name : "+train.getTrain_name()+"     ***  Live train Running Status  ***  -----------------------------------------*    \n \n"
                    + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n "
                    + "|           Station             |           Arrival           |               Departure              |              Train Status                |             Delay           | \n"
                    + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- \n";
        String chk = "Not Started";
        for(TrainLocation trainLocation : route)
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


    // *-------------------------------------------------------------------------------------------*

    //



}
