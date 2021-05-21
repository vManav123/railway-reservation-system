package user.management.system.model.bank;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "bank_sequence")
public class BankSequence {
    @Id
    private String id;
    private long seq;
}
