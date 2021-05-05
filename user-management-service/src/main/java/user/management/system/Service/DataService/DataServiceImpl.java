package user.management.system.Service.DataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.Models.Bank.Bank_Account;
import user.management.system.Models.User.User;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;
import user.management.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import java.time.LocalDate;
import java.util.List;

@Service
public class DataServiceImpl implements DataService{


    // *------------------------- Autowiring Services -----------------------*

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;

    @Autowired
    private DataSequenceGeneratorService sequenceGeneratorService;

    // *---------------------------------------------------------------------*



    // *-------------------- Link Bank Account With User --------------------*

    @Override
    public String linkUsers_To_BankAccount(List<Bank_Account> bank_accounts) {

        List<User> users = userRepository.findAll();
        LinkAccountsToUser(users, bank_accounts);
        return "User Data linked to the Bank Account Successfully";
    }

    @Override
    public String linkAccountToUser() {
        List<User> users = userRepository.findAll();
        List<Bank_Account> bank_accounts = bankRepository.findAll();
        LinkAccountsToUser(users, bank_accounts);
        userRepository.saveAll(users);
        bankRepository.saveAll(bank_accounts);
        return "User Data linked to the Bank Account Successfully";
    }

    private void LinkAccountsToUser(List<User> users, List<Bank_Account> bank_accounts) {
        users.forEach(p->{
            bank_accounts.forEach( q -> {
                // Account updating ....
                if(p.getUser_id().equals(q.getUser_id()))
                {
                    q.setStart_date(LocalDate.now());
                    q.setExpiry_date(q.getStart_date().plusYears(5));
                    q.setContact_no(p.getContact_no());
                    q.setAccount_holder(p.getFull_name());

                    // User updating
                    p.setAccount_no(q.getAccount_no());
                    p.setCredit_card_no(q.getCredit_card_no());
                    p.setCvv(q.getCvv());
                    p.setExpiry_date(p.getExpiry_date());
                    p.setBank_name(p.getBank_name());
                    p.setExpiry_date(q.getExpiry_date());
                    p.setBank_name(q.getBank_name());
                }
            });
        });
        userRepository.saveAll(users);
        bankRepository.saveAll(bank_accounts);
    }

    // *----------------------------------------------------------------------*

}
