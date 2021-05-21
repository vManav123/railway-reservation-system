package railway.application.system.models.response;

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
