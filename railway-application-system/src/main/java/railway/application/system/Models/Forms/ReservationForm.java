package railway.application.system.Models.Forms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReservationForm {
    private UserForm userForm;
    private TicketForm ticketForm;
}
