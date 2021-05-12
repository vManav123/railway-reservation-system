package railway.application.system.Models.Body;

import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Detail {
    private Integer price;
    private LocalTime arrival_time;
    private LocalTime departure_time;
}
