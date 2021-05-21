package railway.application.system.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Passenger {
    private String passenger_name;
    private int age;
    private String email_address;
    private String contact_no;
    private String sex;
    private String quota;
    private String class_name;
}
