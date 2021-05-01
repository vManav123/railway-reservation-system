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
    TrainLocation trainLocation;
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

    public boolean isNumeric(String train_search_info)
    {
        try {
            double d = Double.parseDouble(train_search_info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public String getRoute(Train train,String train_location,String departed)
    {
        System.out.println(departed);
        List<TrainLocation> route = new ArrayList<>();
        LocalDateTime departure = LocalDateTime.of(localDate,train.getDeparture_time());
        String start = train.getStart_from();
        for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
        {
            int platform;
            if(map.getKey().contains("Junction"))
                platform = 3;
            else
                platform = 2;
            LocalDateTime arrival = departure;
            LocalDateTime Departure = departure;
            if(!map.getKey().equals(start))
            {
                arrival = departure.plusMinutes(new Random().nextInt(8)+10);
                Departure = arrival.plusMinutes(new Random().nextInt(3)+2);
            }
            Duration duration = Duration.between(train.getRoute().get(train_location).getDeparture_time(), train.getRoute().get("Gujranwala City").getArrival_time());
            String total_Time = Math.abs(duration.toHoursPart()) + "h " + Math.abs(duration.toMinutesPart()) + "m";
            route.add(new TrainLocation(train.getTrain_no(),train.getTrain_name(),map.getKey(),city,arrival,Departure,total_Time,"Ontime",platform));
            departure = Departure;
        }
        TrainLocation previous_station = null;
        for(TrainLocation map : route)
        {
            if(LocalDateTime.now().isBefore(map.getArrival_time()) && LocalDateTime.now().isAfter(map.getDeparture_time()))
            {
                train_location = map.getCurrent_station();
                break;
            }
            else if(previous_station!=null && LocalTime.now().isBefore(previous_station.getDeparture_time().toLocalTime()) && LocalTime.now().isAfter(map.getArrival_time().toLocalTime()))
            {
               train_location=previous_station.getCurrent_station();
               break;
            }
            previous_station = map;
        }


        String result = "";
        if(route.isEmpty())
            return result+"  !!! There is no Train on this Train information !!!  ";
        else
            result += "*-------------------------------  Train No. : "+train.getTrain_no()+" , Train Name : "+train.getTrain_name()+"            Live train Running Status -----------------------*    \n \n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------- \n "
                    + "|           Station             |           Arrival           |                 Train Status                |             Delay           | \n"
                    + "------------------------------------------------------------------------------------------------------------------------------------------- \n";
        String chk = "Not Started";
        for(TrainLocation trainLocation : route)
        {

            if(!trainLocation.getCurrent_station().equals(train_location) && !chk.equals("Departed") && !chk.equals("Arriving"))
                chk = "Departed";
            else if(trainLocation.getCurrent_station().equals(train_location)) {
                chk = "Arriving";
                result += "|    "+trainLocation.getCurrent_station()+"         |           "+trainLocation.getArrival_time()+"           |            "+trainLocation.getTrain_status()+" ( "+"Departed"+" )"+"                |           No Delay         |  !!!  Your Train is at this station !!!\n"
                        + "------------------------------------------------------------------------------------------------------------------------------------------- \n";
            continue;
            }

            result += "|    "+trainLocation.getCurrent_station()+"         |           "+trainLocation.getArrival_time()+"           |            "+trainLocation.getTrain_status()+" ( "+chk+" )"+"                |           No Delay         | \n"
                        + "------------------------------------------------------------------------------------------------------------------------------------------- \n";
        }
        return result;
    }

    @Override
    public String trainLocation(String train_search_info,String day) {

        if(isNumeric(train_search_info))
        {
            trains = trainRepository.findAll();
            System.out.println(LocalTime.now());
            train = trains.stream().filter(p -> p.getTrain_no().equals(train_search_info)).collect(Collectors.toList()).get(0);
            if(train==null)
                return "No such trains No Exit";
            String previous_station = "null";
            for(Map.Entry<String,Detail> map : train.getRoute().entrySet())
            {
                if(LocalTime.now().isBefore(map.getValue().getArrival_time()) && LocalTime.now().isAfter(map.getValue().getDeparture_time()))
                {
                    return getRoute(train,map.getKey(),"null");
                }
                else if(!previous_station.equals("null") && LocalTime.now().isBefore(train.getRoute().get(previous_station).getDeparture_time()) && LocalTime.now().isAfter(map.getValue().getArrival_time()))
                {
                    return getRoute(train,previous_station,"departed");
                }
                previous_station = map.getKey();
            }
            return null;
        }
        else
        {
            return null;
        }
    }

    // *-------------------------------------------------------------------------------------------*
}
