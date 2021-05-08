package railway.reservation.system.Models.Ticket;

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
    private String train_name;
    private String start;
    private String destination;
    private LocalDate reservation_date;
}
