package railway.reservation.system.model.ticket;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "ticket_sequence")
public class TicketSequence {
    @Id
    private String id;
    private long seq;
}
