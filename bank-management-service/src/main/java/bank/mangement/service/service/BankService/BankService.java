package bank.mangement.service.service.bankService;

import bank.mangement.service.model.bank.Bank_Account;
import bank.mangement.service.model.bank.TransactionalHistory;
import bank.mangement.service.model.bankForm.BankForm;
import bank.mangement.service.model.bankForm.Debit;
import bank.mangement.service.model.payment.Payment;
import org.springframework.stereotype.Service;

@Service
public interface BankService {

    public long getUserIdFromAccountNo(long accountNo);

    public String createAccount(BankForm bankForm);

    public boolean accountNoExist(Long account_no);

    public Double getBalance(Long account_no);

    public String balanceDebited(Debit debit);

    public String addMoney(Long account_no,double amount);

    public String saveTransaction(Payment payment);

    public Long generateTransactionSequence();

    public Payment getTransaction(long transactional_id,long account_no);

    public String deleteBankAccount(long account_no,String confirmation);

    public String updateBankAccount(Bank_Account bank_account);

    public String deleteAllBankAccount(String confirmation);

    public TransactionalHistory getTransactionHistory(long account_no);
}
