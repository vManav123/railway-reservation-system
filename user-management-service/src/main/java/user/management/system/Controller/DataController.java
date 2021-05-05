package user.management.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Service.DataService.DataService;

import java.util.List;
@RestController("dataController")
@RequestMapping(path = "/data")
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping(path = "/linkAccountToUser")
    public String linkAccountToUser(){return dataService.linkAccountToUser();}

    @PostMapping(path = "/linkAccountListToUser")
    public String linkAccount_To_User(@RequestBody List<Bank_Account> bank_accounts){return dataService.linkUsers_To_BankAccount(bank_accounts);}
}
