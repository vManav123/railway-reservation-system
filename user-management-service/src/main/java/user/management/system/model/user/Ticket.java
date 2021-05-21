package user.management.system.model.user;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ticket {
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
    private int age;
    private String quota;

}
