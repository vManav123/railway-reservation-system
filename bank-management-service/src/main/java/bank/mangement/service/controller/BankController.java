package bank.mangement.service.controller;

import bank.mangement.service.configuration.security.models.AuthenticationRequest;
import bank.mangement.service.configuration.security.models.AuthenticationResponse;
import bank.mangement.service.configuration.security.util.JwtUtil;
import bank.mangement.service.model.bank.Bank_Account;
import bank.mangement.service.model.bank.TransactionalHistory;
import bank.mangement.service.model.bankForm.AddMoney;
import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.bankForm.Debit;
import bank.mangement.service.model.payment.Payment;
import bank.mangement.service.repository.CredentialsRepository;
import bank.mangement.service.service.bankService.BankService;
import bank.mangement.service.service.userDetailsService.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@RestController
@RequestMapping(path = "/bank")
@Slf4j
public class BankController {

    // *------------------- Autowiring Reference variables -------------------*
    @Autowired
    private BankService bankService;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;
    private String token = "";
    // *---------------------------------------------------------------*


    // *--------------- Login Interface -------------------*
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("Authentication is in Process");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.error("Authentication is failed");
            return ResponseEntity.of(Optional.of("Wrong Username and Password , Please Type it correctly"));
        }
        log.info("Authentication is done");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        token = jwt;
        log.info("token is generated");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    // *---------------------------------------------------*





    // *---------------------------------------- Bank Management Functionalities ---------------------------------------*

    @PostMapping(path = "/public/createAccount")
    public String createAccount(@RequestBody BankForm bankForm) {
        return bankService.createAccount(bankForm);
    }

    @GetMapping(path = "/public/getBalance/{account_no}")
    public Double getBalance(@PathVariable Long account_no) {
        return bankService.getBalance(account_no);
    }

    @GetMapping(path = "/public/accountExistByAccountNo/{account_no}")
    public boolean accountExistByAccountNo(@PathVariable Long account_no) { return bankService.accountNoExist(account_no); }

    @PostMapping(path = "/public/balanceDebited")
    public String balanceDebited(@RequestBody Debit debit) {
        return bankService.balanceDebited(debit);
    }

    @PostMapping(path = "/public/addMoney")
    public String addAmount(@RequestBody AddMoney addMoney){return bankService.addMoney(addMoney.getAccount_no(),addMoney.getAmount());}

    @PostMapping(path = "/public/saveTransaction")
    @ApiIgnore
    private String saveTransaction(@RequestBody Payment payment) {return bankService.saveTransaction(payment);}

    @GetMapping(path = "/public/getTransaction/{transactional_id}:{account_no}")
    private Payment getTransaction(@PathVariable Long transactional_id,@PathVariable Long account_no){return bankService.getTransaction(transactional_id,account_no);}

    @GetMapping(path = "/public/generateTransactionSequence")
    private Long generateTransactionSequence(){return bankService.generateTransactionSequence();}

    @DeleteMapping(path = "/nonPublic/deleteBankAccount/{account_no}:{confirmation}")
    public String deleteBankAccount(@PathVariable long account_no,@PathVariable String confirmation){return bankService.deleteBankAccount(account_no,confirmation);}

    @DeleteMapping(path = "/nonPublic/deleteAllBankAccount/{confirmation}")
    public String deleteAllBankAccount(@PathVariable String confirmation){return bankService.deleteAllBankAccount(confirmation);}

    @PutMapping(path = "/nonPublic/updateBankAccount")
    public String updateBankAccount(@RequestBody Bank_Account bank_account){return bankService.updateBankAccount(bank_account);}

    @GetMapping(path = "/nonPublic/getTransactionHistory")
    public TransactionalHistory getTransactionHistory(long account_no){return bankService.getTransactionHistory(account_no);}

    @GetMapping(path = "public/getUserIdFromAccountNo/{account_no}")
    public long getUserIdFromAccountNo(@PathVariable long account_no){return bankService.getUserIdFromAccountNo(account_no);}
    // *------------------------------------------ End of Bank Functionalities -----------------------------------------*

}
