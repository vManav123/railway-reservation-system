package railway.reservation.system.model.ticket;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketForm {
    private List<Passenger> passengers;
    private String train_no;
    private String start;
    private String destination;
    private LocalDate reservation_date;
}
