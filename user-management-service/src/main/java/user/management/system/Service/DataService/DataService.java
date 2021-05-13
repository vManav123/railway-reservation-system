package user.management.system.Service.DataService;

import user.management.system.Models.Bank.Bank_Account;

import java.util.List;

public interface DataService {
    public String linkUsers_To_BankAccount(List<Bank_Account> bank_accounts);

    public String linkAccountToUser();
}
