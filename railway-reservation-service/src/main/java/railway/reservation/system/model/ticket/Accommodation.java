package railway.reservation.system.model.ticket;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Accommodation {
    private Long station_id;
    private String station_name;
    private String status;
    private int total_rooms;
    private Map<String,RoomDetail> accommodation;
}
