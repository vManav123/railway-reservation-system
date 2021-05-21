package railway.reservation.system.model.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SeatData {
    private Seat_Id seat_id;
    private String class_name;
    private String seat_no;
}
