package user.management.system.Models.Bank;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionalDetails {
    private LocalDateTime transaction_time;
    private String transaction_type;
    private double amount;
}
