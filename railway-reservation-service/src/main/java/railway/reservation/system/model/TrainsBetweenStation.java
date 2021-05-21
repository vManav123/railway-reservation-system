package railway.reservation.system.model;

import lombok.*;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainsBetweenStation {
    private String train_no;
    private String train_name;
    private String origin;
    private LocalTime departure_time;
    private String destination;
    private LocalTime arrival_time;
    private String travel_time;
    private List<String> run_days;
    private Map<String, Double> classes = new LinkedHashMap<>();

}
