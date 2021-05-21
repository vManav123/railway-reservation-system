package railway.reservation.system.model.ticket;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Passenger {
    private Long passenger_id;
    private String passenger_name;
    private int age;
    private String contact_no;
    private String sex;
    private String quota;
    private String class_name;
}
