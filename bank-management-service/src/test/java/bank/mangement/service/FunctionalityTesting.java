package bank.mangement.service;

import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.bankForm.Debit;
import bank.mangement.service.model.payment.Payment;
import bank.mangement.service.repository.BankRepository;
import bank.mangement.service.service.bankService.BankService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootTest
@TestComponent
public class FunctionalityTesting {
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankService bankService;
    List<String> banks = Arrays.asList("IDBI", "SBI", "ICICI", "ALLAHABAD", "BOI", "OBC", "DBA", "IDFC", "HDFC");
    List<String> account_type = Arrays.asList("saving","current","salary","education");


    @Test
    public void create_TestCase_1(BankForm bankForm)
    {
        bankForm.setBank_name("JOHn");
        Assertions.assertEquals(" !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!"+"\n\nThese are the names of all Account Type , you can choose from here\n" + banks,bankService.createAccount(bankForm));
    }
    @Test
    public void create_TestCase_2(BankForm bankForm)
    {
        bankForm.setBank_name("SBI");
        bankForm.setAccount_type("Local");
        Assertions.assertEquals("!!! Invalid Account Type !!!"+"\n\nThese are the names of all Account Type , you can choose from here\n"+account_type,bankService.createAccount(bankForm));
    }
    @Test
    public void create_TestCase_3(BankForm bankForm)
    {
        bankForm.setBank_name("JOHn");
        Assertions.assertEquals(" !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!"+"\n\nThese are the names of all Account Type , you can choose from here\n" + banks,bankService.createAccount(bankForm));
    }
    @Test
    public void create_TestCase_4(BankForm bankForm)
    {
        bankForm.setBank_name("SBI");
        bankForm.setAccount_type("Local");
        Assertions.assertEquals("!!! Invalid Account Type !!!"+"\n\nThese are the names of all Account Type , you can choose from here\n"+account_type,bankService.createAccount(bankForm));
    }




    @Test
    public void addBalance_TestCase_1()
    {
        Assertions.assertEquals(bankRepository.findById(10001L).get().getBank_balance(),bankService.getBalance(10001L));
    }
    @Test
    public void addBalance_TestCase_2()
    {
        Assertions.assertEquals(-1D,bankService.getBalance(100L));
    }
    @Test
    public void addBalance_TestCase_3()
    {
        Assertions.assertEquals(-1D,bankService.getBalance(1001L));
    }
    @Test
    public void addBalance_TestCase_4()
    {
        Assertions.assertEquals(-1D,bankService.getBalance(1006L));
    }




    @Test
    public void saveTransaction_TestCase_1(Payment payment)
    {
        payment.setAccountNo(123123L);
        Assertions.assertEquals("!!! Invalid Account No !!!",bankService.saveTransaction(payment));
    }
    @Test
    public void saveTransaction_TestCase_2(Payment payment)
    {
        payment.setAccountNo(10001L);
        payment.setAmount(10000D);
        Assertions.assertEquals("success",bankService.saveTransaction(payment));
    }
    @Test
    public void saveTransaction_TestCase_3(Payment payment)
    {
        payment.setAccountNo(123123L);
        Assertions.assertEquals("!!! Invalid Account No !!!",bankService.saveTransaction(payment));
    }
    @Test
    public void saveTransaction_TestCase_4(Payment payment)
    {
        payment.setAccountNo(10001L);
        payment.setAmount(10000D);
        Assertions.assertEquals("success",bankService.saveTransaction(payment));
    }



    @Test
    public void AddMoney_TestCase_1()
    {
        Assertions.assertEquals("Amount will not Not accepted should be greater than 0",bankService.addMoney(10001L,-1));
    }
    @Test
    public void AddMoney_TestCase_2()
    {
        Assertions.assertEquals("Amount will not Not accepted should be greater than 0",bankService.addMoney(10001L,0));
    }
    @Test
    public void AddMoney_TestCase_3()
    {
        Assertions.assertEquals("Amount Added Successfully to your Account with account no : 10001",bankService.addMoney(10001L,11));
    }
    @Test
    public void AddMoney_TestCase_4()
    {
        Assertions.assertEquals("Amount Added Successfully to your Account with account no : 10001",bankService.addMoney(10001L,2));
    }




    @Test
    public void balanceDebited_TestCase_1()
    {
        Assertions.assertEquals("Amount will not Not accepted should be greater than 0",bankService.balanceDebited(new Debit(10001L,-1.0)));
    }
    @Test
    public void balanceDebited_TestCase_2()
    {
        Assertions.assertEquals("Amount will not Not accepted should be greater than 0",bankService.addMoney(10028L,0));
    }
    @Test
    public void balanceDebited_TestCase_3()
    {
        Assertions.assertEquals("!!! Invalid Account No !!!",bankService.addMoney(100028L,1));
    }
    @Test
    public void balanceDebited_TestCase_4()
    {
        bankService.addMoney(10001L,2);
        Assertions.assertEquals("Amount Added Successfully to your Account with account no : 10001",bankService.addMoney(10001L,2));
    }

}
