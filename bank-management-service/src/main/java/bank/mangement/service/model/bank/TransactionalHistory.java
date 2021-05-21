package bank.mangement.service.model.bank;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Transactional-History")
public class TransactionalHistory {
    @Id
    private Long account_no;
    private Map<Long,TransactionalDetails> transactions;
}
