package railway.application.system.Models.Body.Bank;

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
