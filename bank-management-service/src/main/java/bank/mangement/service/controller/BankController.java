package bank.mangement.service.controller;

import bank.mangement.service.model.bankForm.AddMoney;
import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.bankForm.Debit;
import bank.mangement.service.model.payment.Payment;
import bank.mangement.service.service.bankService.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/bank")
public class BankController {

    // *------------------- Autowiring Reference variables -------------------*
    @Autowired
    private BankService bankService;
    // *----------------------------------------------------------------------*




    // *---------------------------------------- Bank Management Functionalities ---------------------------------------*

    @PostMapping(path = "/createAccount")
    public String createAccount(@RequestBody BankForm bankForm) {
        return bankService.createAccount(bankForm);
    }

    @GetMapping(path = "/getBalance/{account_no}")
    public Double getBalance(@PathVariable Long account_no) {
        return bankService.getBalance(account_no);
    }

    @GetMapping(path = "/accountExistByAccountNo/{account_no}")
    public boolean accountExistByAccountNo(@PathVariable Long account_no) { return bankService.accountNoExist(account_no); }

    @PostMapping(path = "/balanceDebited")
    public String balanceDebited(@RequestBody Debit debit) {
        return bankService.balanceDebited(debit);
    }

    @PostMapping(path = "/addMoney")
    public String addAmount(@RequestBody AddMoney addMoney){return bankService.addMoney(addMoney.getAccount_no(),addMoney.getAmount());}

    @PostMapping(path = "/saveTransaction")
    private String saveTransaction(@RequestBody Payment payment) {return bankService.saveTransaction(payment);}

    @GetMapping(path = "/getTransaction/{transactional_id}:{account_no}")
    private Payment getTransaction(@PathVariable Long transactional_id,@PathVariable Long account_no){return bankService.getTransaction(transactional_id,account_no);}

    @GetMapping(path = "/generateTransactionSequence")
    private Long generateTransactionSequence(){return bankService.generateTransactionSequence();}
    // *------------------------------------------ End of Bank Functionalities -----------------------------------------*

}
