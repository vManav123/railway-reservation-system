package railway.application.system.models.forms;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserForm1 {
    private String full_name;
    private int age;
    private String contact_no;
    private String email_address;
    private String secret_key;
}
