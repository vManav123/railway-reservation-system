package railway.reservation.system.model;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TimeTable {
    private int id;
    private String train_name;
    private String train_no;
    private String start_from;
    private String to_destination;
    private LocalTime time_arrival;
    private LocalTime time_departure;


}


