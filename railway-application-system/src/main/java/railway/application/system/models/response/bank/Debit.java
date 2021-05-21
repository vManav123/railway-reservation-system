package railway.application.system.models.response.bank;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Debit {
    private Long account_no;
    private Double amount;
}
