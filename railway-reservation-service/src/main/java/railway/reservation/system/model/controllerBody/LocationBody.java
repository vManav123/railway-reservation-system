package railway.reservation.system.model.controllerBody;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LocationBody {
    private String train_no;
    private LocalDate date;
    private String your_location;
}
