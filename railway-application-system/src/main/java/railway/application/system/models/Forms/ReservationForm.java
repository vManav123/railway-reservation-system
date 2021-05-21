package railway.application.system.models.forms;

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
