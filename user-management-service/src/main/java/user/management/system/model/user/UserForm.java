package user.management.system.model.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserForm {
    private String full_name;
    private int age;
    private String contact_no;
    private String email_address;
    private String secret_key;
}
