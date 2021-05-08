package railway.reservation.system.Models.Ticket;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import railway.reservation.system.Models.Train.Trains_Seats;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Reserve-Seats")
public class ReserveSeats {
    @Id
    private Long reserve_id;
    private LocalDate reservation_date;
    private Trains_Seats trains_seats;

}
