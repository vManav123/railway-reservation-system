package user.management.system.Service.BankService;

import org.springframework.stereotype.Service;
import user.management.system.Models.Bank.BankForm;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Models.User.User;

import java.util.List;

@Service
public interface BankService {
    public String addBankAccount(Bank_Account bank_account);
    public String addAllAccounts(List<Bank_Account> bank_accounts);
    public String createAccount(BankForm bankForm);
    public boolean accountNoExist(Long account_no);
}
