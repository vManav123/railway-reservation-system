package railway.reservation.system.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomDetail {
    private int beds;
    private int bath_room;
    private int toilet;
    private double price;
}
