package railway.reservation.system.model;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainLocation {

    private String current_station;
    private LocalDateTime arrival_time;
    private LocalDateTime departure_time;
    private String train_status;
    private int platform;


}
