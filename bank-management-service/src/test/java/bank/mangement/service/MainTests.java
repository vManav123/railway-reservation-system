package bank.mangement.service;

import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.payment.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import({FunctionalityTesting.class})
class MainTests {

    @Autowired
    private FunctionalityTesting functionalityTesting;
    private BankForm bankForm;
    private Payment payment;

    @Test
    public void GetBalance_Testing()
    {
        functionalityTesting.addBalance_TestCase_1();
        functionalityTesting.addBalance_TestCase_2();
        functionalityTesting.addBalance_TestCase_3();
        functionalityTesting.addBalance_TestCase_4();
    }
    @Test
    public void CreateAccount_Testing()
    {
        bankForm=new BankForm();
        functionalityTesting.create_TestCase_1(bankForm);
        functionalityTesting.create_TestCase_2(bankForm);
        functionalityTesting.create_TestCase_3(bankForm);
        functionalityTesting.create_TestCase_4(bankForm);
    }
    @Test
    public void SaveTransaction_Testing()
    {
        payment = new Payment();
        functionalityTesting.saveTransaction_TestCase_1(payment);
        functionalityTesting.saveTransaction_TestCase_2(payment);
        functionalityTesting.saveTransaction_TestCase_3(payment);
        functionalityTesting.saveTransaction_TestCase_4(payment);
    }

    @Test
    public void AddMoney_Testing()
    {
        functionalityTesting.AddMoney_TestCase_1();
        functionalityTesting.AddMoney_TestCase_2();
        functionalityTesting.AddMoney_TestCase_3();
        functionalityTesting.AddMoney_TestCase_4();
    }

    @Test
    public void balanceDebited()
    {
        functionalityTesting.balanceDebited_TestCase_1();
        functionalityTesting.balanceDebited_TestCase_2();
        functionalityTesting.balanceDebited_TestCase_3();
        functionalityTesting.balanceDebited_TestCase_4();
    }
}
