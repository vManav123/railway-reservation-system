package railway.application.system.Models.Forms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserForm {
    private Long user_id;
    private String AccountHolder;
    private String bank_name;
    private Long account_no;
    private String credit_card_no;
    private String cvv;
}
