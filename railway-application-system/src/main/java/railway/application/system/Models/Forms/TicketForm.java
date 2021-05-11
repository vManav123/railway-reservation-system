package railway.application.system.Models.Forms;

import lombok.*;
import railway.application.system.Models.Passenger;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketForm {
    private String train_no;
    private String train_name;
    private String start;
    private String destination;
    private Date reservation_date;
    private List<Passenger> passengers;
}
