package user.management.system.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "Bank-Account")
public class Bank_Account {
    @Id
    @Field(name = "account_no")
    private Long account_no;
    @Field(name = "bank_name")
    private String bank_name;
    @Field(name = "credit_card_no")
    private String credit_card_no;
    @Field(name = "cvv")
    private String cvv;
    @Field(name = "expiry_date")
    private LocalDateTime expiry_date;
    @Field(name = "isActive")
    private Boolean isActive;
    @Field(name = "bank_balance")
    private Double bank_balance;
}
