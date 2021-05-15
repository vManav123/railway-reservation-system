package user.management.system.Models.Bank;

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
