package railway.management.system.Models;

import java.time.LocalTime;

public class TimeTable {
    private int id;
    private String train_name;
    private String train_no;
    private String start_from;
    private String to_destination;
    private LocalTime time_arrival;
    private LocalTime time_departure;

    public TimeTable(){}

    public TimeTable(int id, String train_name, String train_no, String start_from, LocalTime time_arrival, String to_destination, LocalTime time_departure) {
        this.id = id;
        this.train_name = train_name;
        this.train_no = train_no;
        this.start_from = start_from;
        this.time_arrival = time_arrival;
        this.to_destination = to_destination;
        this.time_departure = time_departure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStart_from() {
        return start_from;
    }

    public void setStart_from(String start_from) {
        this.start_from = start_from;
    }

    public String getTo_destination() {
        return to_destination;
    }

    public void setTo_destination(String to_destination) {
        this.to_destination = to_destination;
    }

    public LocalTime getTime_arrival() {
        return time_arrival;
    }

    public void setTime_arrival(LocalTime time_arrival) {
        this.time_arrival = time_arrival;
    }

    public LocalTime getTime_departure() {
        return time_departure;
    }

    public void setTime_departure(LocalTime time_departure) {
        this.time_departure = time_departure;
    }

    @Override
    public String toString() {
        return "TimeTable { " +
                "id=" + id +
                ", train_name='" + train_name + '\'' +
                ", train_no='" + train_no + '\'' +
                ", start_from='" + start_from + '\'' +
                ", time_arrival=" + time_arrival +
                ", to_destination='" + to_destination + '\'' +
                ", time_departure=" + time_departure + ' '+
                '}';
    }
}


