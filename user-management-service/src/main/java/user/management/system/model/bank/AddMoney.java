package user.management.system.model.bank;

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
