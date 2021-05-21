package railway.application.system.models;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {

    private Long pnr;
    private String train_no;
    private String train_name;
    private String start;
    private String destination;
    private String class_name;
    private LocalDateTime departure_time;
    private LocalDateTime arrival_time;
    private String passenger_name;
    private String contact_no;
    private String sex;
    private String email_address;
    private LocalDate date_of_journey;
    private String journey_time;
    private String seat_no;
    private int age;
    private String quota;
    private String status;
    private Long transactional_id;
}
