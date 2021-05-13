package user.management.system.Service.BankService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.ExceptionHandling.BankNotExistException;
import user.management.system.ExceptionHandling.UserNotExistException;
import user.management.system.Models.Bank.BankForm;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Models.Body.BankBody.Debit;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;
import user.management.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service("bankServiceImpl")
public class BankServiceImpl implements BankService {

    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "      !!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Contact Number !!!";
    // *------------------------------------------------------------------*



    // *-------------------------- Banks Names --------------------------*
    List<String> banks = Arrays.asList("IDBI", "SBI", "ICICI", "ALLAHABAD", "BOI", "OBC", "DBA", "IDFC", "HDFC");
    // *-----------------------------------------------------------------*



    // *---------------- Autowired Reference Variables ----------------*
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;

    @Autowired
    private DataSequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private Bank_Account bank_account;
    // *-----------------------------------------------------------------*



    // *---------------------- Basic Functionalities --------------------*
    @Override
    public String addBankAccount(Bank_Account bank_account) {
        return "*--------- Bank added Successfully ---------*";
    }


    @Override
    public String addAllAccounts(List<Bank_Account> bank_accounts) {
        bank_accounts.forEach(p -> p.setAccount_no(sequenceGeneratorService.getBankSequenceNmber("bank_sequence")));
        bankRepository.saveAll(bank_accounts);
        return "*------ All Bank Accounts Added Successfully -------*";
    }
    // *-----------------------------------------------------------------*


    // *-------------- Bank Account Creation Functionality --------------*
    @Override
    public String createAccount(BankForm bankForm) {
        try {
            if (!banks.contains(bankForm.getBank_name().toUpperCase()))
                throw new BankNotExistException(bankNotExistException);
        } catch (BankNotExistException e) {
            return e.getMessage() + "\n" + banks.toString();
        }
        try {
            if (!userRepository.existsById(bankForm.getUser_id()))
                throw new UserNotExistException(userNotExistException);
        } catch (UserNotExistException e) {
            return e.getMessage();
        }

        bank_account.setAccount_holder(bankForm.getAccountHolder());
        bank_account.setAccount_no(sequenceGeneratorService.getBankSequenceNmber("bank_sequence"));
        bank_account.setBank_name(bankForm.getBank_name());
        bank_account.setUser_id(bankForm.getUser_id());
        bank_account.setContact_no(bankForm.getContact_no());
        bank_account.setAccount_type(bankForm.getAccount_type());
        bank_account.setCredit_card_no(new Random().nextInt(8999) + 1000 + "-" + new Random().nextInt(8999) + 1000 + "-" + new Random().nextInt(8999) + 1000);
        bank_account.setCvv(new Random().nextInt(899) + 100 + "");
        bank_account.setStart_date(LocalDate.now());
        bank_account.setExpiry_date(bank_account.getStart_date().plusYears(5));
        bank_account.setBank_balance(0.0);
        bank_account.setIsActive(Boolean.TRUE);

        bankRepository.save(bank_account);
        return "Account Created Successfully";
    }

    @Override
    public boolean accountNoExist(Long account_no) {
        return bankRepository.existsById(account_no);
    }

    @Override
    public Double getBalance(Long account_no) {
        return bankRepository.findAll().stream().filter(p -> p.getAccount_no().equals(account_no)).collect(Collectors.toList()).get(0).getBank_balance();
    }

    @Override
    public String balanceDebited(Debit debit) {
        Bank_Account bank_account = bankRepository.findAll().stream().filter(p -> p.getAccount_no().equals(debit.getAccount_no())).collect(Collectors.toList()).get(0);
        bank_account.setBank_balance(bank_account.getBank_balance() - debit.getAmount());
        bankRepository.save(bank_account);
        System.out.println("Done Payment");
        return "success";
    }
    // *----------------------------------------------------------------*

}
