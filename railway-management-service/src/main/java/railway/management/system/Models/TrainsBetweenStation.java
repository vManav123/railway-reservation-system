package railway.management.system.Models;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TrainsBetweenStation {
    private String train_no;
    private String train_name;
    private String origin;
    private LocalTime departure_time;
    private String destination;
    private LocalTime arrival_time;
    private String travel_time;
    private List<String> run_days;
    private Map<String,Double> classes = new LinkedHashMap<>();

    public TrainsBetweenStation(String train_no, String train_name, String origin, LocalTime departure_time, String destination, LocalTime arrival_time, String travel_time, List<String> run_days, Map<String, Double> classes) {
        this.train_no = train_no;
        this.train_name = train_name;
        this.origin = origin;
        this.departure_time = departure_time;
        this.destination = destination;
        this.arrival_time = arrival_time;
        this.travel_time = travel_time;
        this.run_days = run_days;
        this.classes = classes;
    }


    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public LocalTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalTime departure_time) {
        this.departure_time = departure_time;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalTime getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(LocalTime arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(String travel_time) {
        this.travel_time = travel_time;
    }

    public List<String> getRun_days() {
        return run_days;
    }

    public void setRun_days(List<String> run_days) {
        this.run_days = run_days;
    }

    public Map<String, Double> getClasses() {
        return classes;
    }

    public void setClasses(Map<String, Double> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "TrainsBetweenStation{" +
                "train_no='" + train_no + '\'' +
                ", train_name='" + train_name + '\'' +
                ", origin='" + origin + '\'' +
                ", departure_time=" + departure_time +
                ", destination='" + destination + '\'' +
                ", arrival_time=" + arrival_time +
                ", travel_time='" + travel_time + '\'' +
                ", run_days=" + run_days +
                ", classes=" + classes +
                '}';
    }
}
