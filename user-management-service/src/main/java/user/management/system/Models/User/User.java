package user.management.system.Models.User;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import user.management.system.Models.Ticket;

import java.time.LocalTime;
import java.util.List;

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
    @Field(name = "account_no")
    private Long account_no;
    @Field(name = "user_name")
    private String user_name;
    @Field(name = "password")
    private String password;
    @Field(name = "roles")
    private String roles;
    @Field(name = "failed_attempts")
    private int failed_attempts;
    @Field(name = "account_non_locked")
    private boolean account_non_locked;
    @Field(name = "lock_time")
    private LocalTime lock_time;
    @Field(name = "tickets")
    private Map<Long, Ticket> tickets;
}
