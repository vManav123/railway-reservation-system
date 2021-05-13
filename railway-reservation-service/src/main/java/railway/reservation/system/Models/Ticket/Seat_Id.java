package railway.reservation.system.Models.Ticket;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Seat_Id {
    private String train_no;
    private LocalDate reservation_date;
}
