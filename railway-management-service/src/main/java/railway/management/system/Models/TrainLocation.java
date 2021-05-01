package railway.management.system.Models;

import java.time.LocalDateTime;

public class TrainLocation {

    private String train_no;
    private String train_name;
    private String current_station;
    private String your_location;
    private LocalDateTime arrival_time;
    private LocalDateTime departure_time;
    private String time_to_reach;
    private String train_status;
    private int platform;

    public TrainLocation(String train_no, String train_name, String current_station, String your_location, LocalDateTime arrival_time, LocalDateTime departure_time, String time_to_reach, String train_status, int platform) {
        this.train_no = train_no;
        this.train_name = train_name;
        this.current_station = current_station;
        this.your_location = your_location;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.time_to_reach = time_to_reach;
        this.train_status = train_status;
        this.platform = platform;
    }

    public TrainLocation() {
    }

    public LocalDateTime getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(LocalDateTime arrival_time) {
        this.arrival_time = arrival_time;
    }

    public void setTime_to_reach(String time_to_reach) {
        this.time_to_reach = time_to_reach;
    }

    public String getTrain_status() {
        return train_status;
    }

    public void setTrain_status(String train_status) {
        this.train_status = train_status;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
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

    public LocalDateTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }

    public String getTime_to_reach() {
        return time_to_reach;
    }

}
