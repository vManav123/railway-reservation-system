package railway.application.system.models.response;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AvailableSeats {
    private String start;
    private String destination;
    private LocalDate localDate;
}
