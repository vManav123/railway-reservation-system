package railway.management.system.Models;

import java.time.LocalDateTime;

public class TrainLocation {

    private String current_station;
    private LocalDateTime arrival_time;
    private LocalDateTime departure_time;
    private String train_status;
    private int platform;

    public TrainLocation(String current_station, LocalDateTime arrival_time, LocalDateTime departure_time, String train_status, int platform) {
        this.current_station = current_station;
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
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



    public String getCurrent_station() {
        return current_station;
    }

    public void setCurrent_station(String current_station) {
        this.current_station = current_station;
    }

    public LocalDateTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }


}
