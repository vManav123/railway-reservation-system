package railway.application.system.Models.Payment;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "payment_sequence")
public class PaymentSequence {
    @Id
    private String id;
    private long seq;
}