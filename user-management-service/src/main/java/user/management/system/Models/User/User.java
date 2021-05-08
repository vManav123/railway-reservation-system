package user.management.system.Models.User;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;
import java.util.Map;
import user.management.system.Models.Ticket;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "User")
public class User {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    @Field(name = "user_id")
    private Long user_id;
    @Field(name = "full_name")
    private String full_name;
    @Field(name = "age")
    private int age;
    @Field(name = "contact_no")
    private String contact_no;
    @Field(name = "bank_name")
    @Value("${default:null}")
    private String bank_name;
    @Field(name = "account_no")
    @Value("${default:0}")
    private Long account_no;
    @Field(name = "credit_card_no")
    @Value("${default:null}")
    private String credit_card_no;
    @Field(name = "cvv")
    @Value("${default:null}")
    private String cvv;
    @Field(name = "expiry_date")
    private LocalDate expiry_date;
    @Field(name = "username")
    private String username;
    @Field(name = "password")
    private String password;
    @Field(name = "roles")
    private String roles;
    @Value("${default:0}")
    @Field(name = "failed_attempts")
    private int failed_attempts;
    @Value("${default:false}")
    @Field(name = "account_non_locked")
    private boolean account_non_locked;
    @Field(name = "lock_time")
    private LocalTime lock_time;
    @Field(name = "tickets")
    private Map<Long,Ticket> tickets = new Hashtable<Long,Ticket>();
}
