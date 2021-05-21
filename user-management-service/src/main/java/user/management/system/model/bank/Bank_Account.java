package user.management.system.model.bank;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Bank-Account")
public class Bank_Account {

    @Transient
    public static final String SEQUENCE_NAME = "data_sequence";

    @Id
    @Field(name = "account_no")
    private Long account_no;
    @Field(name = "bank_name")
    private String bank_name;
    @Field(name = "user_id")
    private Long user_id;
    @Field(name = "email_address")
    private String email_address;
    @Field(name = "account_holder")
    private String account_holder;
    @Field(name = "contact_no")
    private String contact_no;
    @Field(name = "credit_card_no")
    private String credit_card_no;
    @Field(name = "cvv")
    private String cvv;
    @Field(name = "start_date")
    private LocalDate start_date;
    @Field(name = "expiry_date")
    private LocalDate expiry_date;
    @Field(name = "isActive")
    private Boolean isActive;
    @Field(name = "bank_balance")
    private Double bank_balance;
    @Field(name = "account_type")
    private String account_type;

}
