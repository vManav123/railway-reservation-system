package railway.reservation.system.model.ticket;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketStatus {
    private Long pnr;
    private String status;
    private String seat_no;
}
