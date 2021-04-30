package railway.management.system.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import railway.management.system.Models.*;
import railway.management.system.Repository.TrainRepository;

import java.time.Duration;
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

    public List<TimeTable> displayTimeTableByYourCity(String city)
    {
        this.city = city;
        return displayTimeTable();
    }


    @Override
    public String trainLocationByTrainName(String train_name) {

        trains = trainRepository.findAll();
        train = trains.stream().filter( p -> p.getTrain_name().equalsIgnoreCase(train_name)).collect(Collectors.toList()).get(0);



        if(train != null)
        {
            trainLocation.setTrain_no(train.getTrain_no());
            trainLocation.setTrain_name(train.getTrain_name());
            trainLocation.setYour_location(city);

            LocalTime previous_time = LocalTime.now().plusMinutes(-5);
            LocalTime next_time = previous_time;
            LocalTime current_time = LocalTime.now();
            for(Map.Entry<String, Detail> map : train.getRoute().entrySet())
            {
                if(map.getValue().getArrival_time().isBefore(current_time) && map.getValue().getDeparture_time().isAfter(current_time))
                {
                    trainLocation.setCurrent_station(map.getKey());
                    trainLocation.setDeparture_time(map.getValue().getDeparture_time());
                    return trainLocation.toString();
                }
                else if(previous_time.isBefore(current_time) && next_time.isAfter(current_time))
                {

                    return null;
                }
            }
        }


        return null;
    }

    @Override
    public String trainLocationByTrainNo(String train_no) {

        return null;
    }
}
