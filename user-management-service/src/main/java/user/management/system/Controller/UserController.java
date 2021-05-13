package user.management.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.Models.Bank.BankForm;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Models.Body.BankBody.Debit;
import user.management.system.Models.User.User;
import user.management.system.Models.User.UserForm;
import user.management.system.Service.BankService.BankService;
import user.management.system.Service.UserService.UserService;

import java.util.List;


@RestController("userController")
@RequestMapping("/user")
public class UserController {

    // *---------------------------------- User Functionalities -------------------------------*
    @Autowired
    private UserService userService;
    @Autowired
    private BankService bankService;

    // *-----------------  Basic Functionality -----------------*
    @GetMapping(path = "/welcome")
    public String user() {
        return "Welcome to User Interface";
    }

    @PostMapping(path = "/addUser")
    public String addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping(path = "/addAllUser")
    public String addAllUser(@RequestBody List<User> users) {
        return userService.addAllUser(users);
    }

    @PostMapping(path = "/createUser")
    public String createUser(@RequestBody UserForm userForm) {
        return userService.createUser(userForm);
    }

    @GetMapping(path = "/getAllUsers")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }
    // *--------------------------------------------------------*

    // *------------------------------------------ End of User Functionalities -----------------------------------------*


    // *---------------------------------------- Bank Management Functionalities ---------------------------------------*

    @GetMapping(path = "/userExistById/{user_id}")
    public boolean userExistById(@PathVariable Long user_id) {
        return userService.userExistById(user_id);
    }

    // *------------------ Basic Functionality ----------------- *

    @PostMapping(path = "/addAllAccounts")
    public String addAllAccounts(@RequestBody List<Bank_Account> bank_accounts) {
        return bankService.addAllAccounts(bank_accounts);
    }

    @PostMapping(path = "/createAccount")
    public String createAccount(@RequestBody BankForm bankForm) {
        return bankService.createAccount(bankForm);
    }

    @GetMapping(path = "/getBalance/{account_no}")
    public Double getBalance(@PathVariable Long account_no) {
        return bankService.getBalance(account_no);
    }

    @GetMapping(path = "/accountExistByAccountNo/{account_no}")
    public boolean accountExistByAccountNo(@PathVariable Long account_no) {
        return bankService.accountNoExist(account_no);
    }

    @PostMapping(path = "/balanceDebited")
    public String balanceDebited(@RequestBody Debit debit) {
        return bankService.balanceDebited(debit);
    }
    // *----------------------------------------------------------*

    // *------------------------------------------ End of Bank Functionalities -----------------------------------------*

}
