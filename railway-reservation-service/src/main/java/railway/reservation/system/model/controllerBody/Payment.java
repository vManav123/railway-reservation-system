package railway.reservation.system.model.controllerBody;

import lombok.*;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "payment")
public class Payment {

    @Transient
    public static final String SEQUENCE_NAME = "payment_sequence";

    @Id
    private Long transactional_id;
    private Double amountDebited;
    private Long AccountNo;
    private LocalDateTime transaction_time;
}
