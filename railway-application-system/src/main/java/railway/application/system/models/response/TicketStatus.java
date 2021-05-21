package railway.application.system.models.response;

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
