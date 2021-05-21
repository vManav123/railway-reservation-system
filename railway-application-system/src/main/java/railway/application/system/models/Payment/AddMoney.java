package railway.application.system.models.payment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddMoney {
    private Long account_no;
    private Double amount;
}
