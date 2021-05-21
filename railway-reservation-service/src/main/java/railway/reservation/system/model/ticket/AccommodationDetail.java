package railway.reservation.system.model.ticket;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Accommodation-Detail")
public class AccommodationDetail {
    @Id
    private String train_no;
    private List<Accommodation> accommodations;
}
