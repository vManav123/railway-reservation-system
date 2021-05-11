package user.management.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.Models.Bank.BankForm;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Service.BankService.BankService;

import java.util.List;

@RestController("bankController")
@RequestMapping("/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    // *------------------ Basic Functionality ----------------- *
    @GetMapping(path = "/welcome")
    public String user(){return "Welcome to Bank Interface";}

    @PostMapping(path = "/addAllAccounts")
    public String addAllAccounts(@RequestBody List<Bank_Account> bank_accounts){return bankService.addAllAccounts(bank_accounts);}

    @PostMapping(path = "/createAccount")
    public String createAccount(@RequestBody BankForm bankForm)
    {
        return bankService.createAccount(bankForm);
    }

    @GetMapping(path = "/accountExistByAccountNo/{account_no}")
    public boolean accountExistByAccountNo(@PathVariable Long account_no){return bankService.accountNoExist(account_no);}
    // *----------------------------------------------------------*
}
