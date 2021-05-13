package railway.application.system.Models.Body;

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
