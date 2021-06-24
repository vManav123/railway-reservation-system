package bank.mangement.service.service.bankService;

import bank.mangement.service.exception.BankNotExistException;
import bank.mangement.service.exception.InvalidAccountNoException;
import bank.mangement.service.exception.InvalidAccountTypeException;
import bank.mangement.service.exception.UserNotExistException;
import bank.mangement.service.model.bank.Bank_Account;
import bank.mangement.service.model.bank.TransactionalDetails;
import bank.mangement.service.model.bank.TransactionalHistory;
import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.bankForm.Debit;
import bank.mangement.service.model.bankForm.User;
import bank.mangement.service.model.payment.Payment;
import bank.mangement.service.repository.BankRepository;
import bank.mangement.service.repository.CredentialsRepository;
import bank.mangement.service.repository.TransactionalRepository;
import bank.mangement.service.service.emailService.EmailService;
import bank.mangement.service.service.sequenceGenerator.DataSequenceGeneratorService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("bankServiceImpl")
@Slf4j
public class BankServiceImpl implements BankService {

    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "!!! There is no User Exist with this ID !!!    ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Contact Number !!!";
    String invalidAccountNoException = "!!! Invalid Account No !!!";
    String invalidAccountTypeException = "!!! Invalid Account Type !!!";
    // *------------------------------------------------------------------*



    // *---------------- Autowired Reference Variables ----------------*
    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;
    @Autowired
    private DataSequenceGeneratorService sequenceGeneratorService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TransactionalRepository transactionalRepository;
    @Autowired
    private Bank_Account bank_account;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CredentialsRepository credentialsRepository;
    // *-----------------------------------------------------------------*


    // *------------------------ Validation Content ---------------------*
    List<String> account_type = Arrays.asList("saving","current","salary","education");
    List<String> banks = Arrays.asList("IDBI", "SBI", "ICICI", "ALLAHABAD", "BOI", "OBC", "DBA", "IDFC", "HDFC");
    // *-----------------------------------------------------------------*


    @Override
    public long getUserIdFromAccountNo(long accountNo) {
        return bankRepository.findById(accountNo).get().getUser_id();
    }

    // *-------------- Bank Account Creation Functionality --------------*
    @Override
    @HystrixCommand(fallbackMethod = "getFallbackAccountCreation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "20000")
    })
    public String createAccount(BankForm bankForm) {
        String result;
        log.info("Bank Form Validation is in Process");
        if (!(result=validateBankForm(bankForm)).equals("success"))
            return result;
        log.info("Bank Form Validated Successfully");

        bank_account.setAccount_holder(bankForm.getAccountHolder());
        bank_account.setAccount_no(sequenceGeneratorService.getBankSequenceNumber("bank_sequence"));
        bank_account.setBank_name(bankForm.getBank_name());
        bank_account.setUser_id(bankForm.getUser_id());
        bank_account.setContact_no(bankForm.getContact_no());
        bank_account.setAccount_type(bankForm.getAccount_type());
        bank_account.setCredit_card_no((new Random().nextInt(8999) + 1000) + "-" + (new Random().nextInt(8999) + 1000) + "-" + (new Random().nextInt(8999) + 1000));
        bank_account.setCvv(new Random().nextInt(899) + 100 + "");
        bank_account.setStart_date(LocalDate.now());
        bank_account.setExpiry_date(bank_account.getStart_date().plusYears(5));
        bank_account.setBank_balance(0.0);
        bank_account.setIsActive(Boolean.TRUE);
        log.info("bank detail Filled");

        User user = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/getUser/"+bankForm.getUser_id(), User.class);
        user.setAccount_no(bank_account.getAccount_no());
        user.setBank_name(bank_account.getBank_name());
        user.setCredit_card_no(bank_account.getCredit_card_no());
        user.setCvv(bank_account.getCvv());
        user.setExpiry_date(bank_account.getExpiry_date());
        log.info("User detail Filled");
        if(!restTemplate.postForObject("http://RAILWAY-API-GATEWAY/user/public/updateUser",user,String.class).equals("User Details Updated for this user id : "+user.getUser_id()))
        {
            log.info("User Details Not Updated Successfully");
            return "User Details Not Updated Successfully";
        }
        bank_account.setEmail_address(user.getEmail_address());
        bankRepository.save(bank_account);
        log.info("Bank Account Created Successfully");

        emailService.sendSimpleEmail(bank_account.getEmail_address(),"Dear "+user.getFull_name()+",\nWelcome Railway Bank System\nYour Account has been created Successfully with this account no. :"+bank_account.getAccount_no()+" and account Type. "+bank_account.getAccount_type()+"\n  Bank Name : "+bank_account.getBank_name()+".\n\nWith Regards\nBank Management System\nbank.railway.service@gmail.com","Bank is Created Successfully" );

        return "Account Created Successfully";
    }

    public String getFallbackAccountCreation(BankForm bankForm){return "!!! Service is Down , Please Try again later";}


    @HystrixCommand(fallbackMethod = "getFallbackValidation" , commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000")
    })
    private String validateBankForm(BankForm bankForm) {
        try {
            if (!banks.contains(bankForm.getBank_name().toUpperCase()))
                throw new BankNotExistException(bankNotExistException);
        } catch (BankNotExistException e) {
            return e.getMessage() + "\n\nThese are the names of all Account Type , you can choose from here\n" + banks;
        }
        log.info("Bank Name is Valid");
        try {
            if(!account_type.contains(bankForm.getAccount_type().toLowerCase()))
                throw new InvalidAccountTypeException(invalidAccountTypeException);
        }
        catch (InvalidAccountTypeException e)
        {
            return e.getMessage()+"\n\nThese are the names of all Account Type , you can choose from here\n"+account_type;
        }
        log.info("Account Type is Valid "+bankForm.getUser_id());
        try {
            if (!restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/userExistById/"+ bankForm.getUser_id(),Boolean.class))
                throw new UserNotExistException(userNotExistException);
        } catch (UserNotExistException e) {
            return e.getMessage();
        }
        log.info("Success in validation of Bank Form");
        return "success";
    }

    public String getFallbackValidation(BankForm bankForm){return "!!! Service is Down , Please Try again later !!!";}

    @Override
    public boolean accountNoExist(Long account_no) {
        return bankRepository.existsById(account_no);
    }

    @Override
    public Double getBalance(Long account_no) {
        try{
            if(!accountNoExist(account_no))
                throw new InvalidAccountNoException(invalidAccountNoException);
        }
        catch (InvalidAccountNoException e)
        {
            return -1D;
        }
        return bankRepository
                .findAll()
                .parallelStream()
                .filter(p -> p.getAccount_no().equals(account_no))
                .collect(Collectors.toList())
                .get(0).getBank_balance();
    }

    @Override
    public String balanceDebited(Debit debit) {
        if(debit.getAmount()<=0)
            return "Amount will not Not accepted should be greater than 0";
        // Transaction
        List<TransactionalHistory> transactionalHistories = transactionalRepository.findAll().parallelStream().filter(p->p.getAccount_no().equals(debit.getAccount_no())).collect(Collectors.toList());
        if(transactionalHistories.isEmpty())
        {
            Map<Long, TransactionalDetails> map = new Hashtable<>();
            map.put(sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence"),new TransactionalDetails(LocalDateTime.now(),"Debited",debit.getAmount()));
            transactionalRepository.save(new TransactionalHistory(debit.getAccount_no(),map));
        }
        else
        {
            transactionalHistories
                    .get(0)
                    .getTransactions()
                    .put(sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence"),new TransactionalDetails(LocalDateTime.now(),"Debited",debit.getAmount()));
        }

        // bank
        List<Bank_Account> bank_accounts = bankRepository
                .findAll()
                .parallelStream()
                .filter(p -> p.getAccount_no().equals(debit.getAccount_no()))
                .collect(Collectors.toList());
        try
        {
            if(bank_accounts.isEmpty())
                throw new InvalidAccountNoException(invalidAccountNoException);
        }
        catch (InvalidAccountNoException e)
        {
            return e.getMessage();
        }
        bank_account=bank_accounts.get(0);
        emailService.sendSimpleEmail(bank_account.getEmail_address(),"The Transaction happened Your Account with this Account no : "+bank_account.getAccount_no()+" and amount of Rs. "+debit.getAmount()+"  was Debited at "+LocalDateTime.now()+".","Amount Debited from your account" );
        bank_account.setBank_balance(bank_account.getBank_balance() - debit.getAmount());
        bankRepository.save(bank_account);
        return "success";
    }

    @Override
    public String addMoney(Long account_no, double amount) {
        // Transaction
        try {
            if(amount<=0)
                throw new InvalidAccountNoException("Amount will not Not accepted should be greater than 0");
        }
        catch (InvalidAccountNoException e)
        {
            return e.getMessage();
        }

        List<TransactionalHistory> transactionalHistories = transactionalRepository.findAll().parallelStream().filter(p->p.getAccount_no().equals(account_no)).collect(Collectors.toList());
        if(transactionalHistories.isEmpty())
        {
            Map<Long, TransactionalDetails> map = new Hashtable<>();
            map.put(sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence"),new TransactionalDetails(LocalDateTime.now(),"Credited",amount));
            transactionalRepository.save(new TransactionalHistory(account_no,map));
        }
        else
        {
            transactionalHistories
                    .get(0)
                    .getTransactions()
                    .put(sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence"),new TransactionalDetails(LocalDateTime.now(),"Credited",amount));
        }

        // bank
        List<Bank_Account> bank_accounts = bankRepository
                .findAll()
                .parallelStream()
                .filter(p -> p.getAccount_no().equals(account_no))
                .collect(Collectors.toList());
        try
        {
            if(bank_accounts.isEmpty())
                throw new InvalidAccountNoException(invalidAccountNoException);
        }
        catch (InvalidAccountNoException e)
        {
            return e.getMessage();
        }
        bank_account=bank_accounts.get(0);
        emailService.sendSimpleEmail(bank_account.getEmail_address(),"The Transaction happened Your Account with this Account no : "+bank_account.getAccount_no()+" and amount of Rs. "+amount+"  was Credited at "+LocalDateTime.now()+".","Amount Credited to your account" );
        bank_account.setBank_balance(bank_account.getBank_balance() + amount);
        bankRepository.save(bank_account);

        return "Amount Added Successfully to your Account with account no : "+account_no;
    }

    @Override
    public String saveTransaction(Payment payment) {
        try {
            if(!transactionalRepository.existsById(payment.getAccountNo()))
                throw new InvalidAccountNoException(invalidAccountNoException);
        }
        catch (InvalidAccountNoException e) {
            return e.getMessage();
        }
        payment.setTransactional_id(sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence"));
        TransactionalHistory transactionalHistory = transactionalRepository.findById(payment.getAccountNo()).get();
        transactionalHistory.getTransactions().put(payment.getTransactional_id(), new TransactionalDetails(payment.getTransaction_time(),payment.getTransaction_type(),payment.getAmount()));
        transactionalRepository.save(transactionalHistory);
        return "success";
    }

    @Override
    public Long generateTransactionSequence() {
        return sequenceGeneratorService.getTransactionSequenceNumber("transaction_sequence");
    }

    @Override
    public Payment getTransaction(long transactional_id,long account_no) {
        TransactionalDetails transactionalHistory = transactionalRepository.findById(account_no).get().getTransactions().get(transactional_id);
        return new Payment(transactional_id,transactionalHistory.getAmount(),account_no,transactionalHistory.getTransaction_type(),transactionalHistory.getTransaction_time());
    }

    @Override
    public String deleteBankAccount(long account_no,String confirmation) {
        if(!confirmation.equalsIgnoreCase("Yes"))
            return "Deletion Unsuccessful";

        // validate Account no
        if (validateAccountNo(account_no)) return invalidAccountNoException;

        // deletion of Bank account
        bankRepository.deleteById(account_no);
        return  "Deletion Successful";
    }

    private boolean validateAccountNo(long account_no) {
        try {
            if(!bankRepository.existsById(account_no))
                throw new InvalidAccountNoException(invalidAccountNoException);
        }
        catch (InvalidAccountNoException e)
        {
            return true;
        }
        return false;
    }

    @Override
    public String updateBankAccount(Bank_Account bank_account) {

        // validate Account no
        if (validateAccountNo(bank_account.getAccount_no())) return invalidAccountNoException;
        return "Bank Account is updated Successfully";
    }

    @Override
    public String deleteAllBankAccount(String confirmation) {
        if(!confirmation.equalsIgnoreCase("Yes"))
            return "Deletion Unsuccessful";
        bankRepository.deleteAll();
        return "Deletion Successful";
    }

    @Override
    public TransactionalHistory getTransactionHistory(long account_no) {
        return transactionalRepository.findById(account_no).get();
    }
    // *----------------------------------------------------------------*

}
