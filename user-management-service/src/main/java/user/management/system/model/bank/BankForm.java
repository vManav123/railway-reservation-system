package user.management.system.model.bank;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BankForm {
    private String AccountHolder;
    private Long user_id;
    private String contact_no;
    private String bank_name;
    private String account_type;
}
