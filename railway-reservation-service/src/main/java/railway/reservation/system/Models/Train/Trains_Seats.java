package railway.reservation.system.Models.Train;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Train-Seats")
public class Trains_Seats {
    @Id
    private Long train_id;
    private Train train;
    private Map<String,Seats> seats; // coach name & total seat , cabin seats
    private Map<String,Integer> coaches;
}
