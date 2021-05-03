package railway.reservation.system.Models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Passenger {
    private Long passenger_id;
    private String passenger_name;
    private String age;
    private String contact_no;
    private String sex;
    private String address;
    private String quota;
    private String credit_card_no;
    private String bank_name;
    private String class_name;


}
