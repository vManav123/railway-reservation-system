package railway.management.system.Models;

import java.time.LocalTime;

public class TrainLocation {
    private String train_name;
    private String train_no;
    private String current_station;
    private String your_location;
    private LocalTime departure_time;
    private LocalTime time_to_reach;

    public TrainLocation(String train_name, String train_no, String current_station, String your_location, LocalTime departure_time, LocalTime time_to_reach) {
        this.train_name = train_name;
        this.train_no = train_no;
        this.current_station = current_station;
        this.your_location = your_location;
        this.departure_time = departure_time;
        this.time_to_reach = time_to_reach;
    }

    public TrainLocation() {
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getCurrent_station() {
        return current_station;
    }

    public void setCurrent_station(String current_station) {
        this.current_station = current_station;
    }

    public String getYour_location() {
        return your_location;
    }

    public void setYour_location(String your_location) {
        this.your_location = your_location;
    }

    public LocalTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalTime departure_time) {
        this.departure_time = departure_time;
    }

    public LocalTime getTime_to_reach() {
        return time_to_reach;
    }

    public void setTime_to_reach(LocalTime time_to_reach) {
        this.time_to_reach = time_to_reach;
    }
}
