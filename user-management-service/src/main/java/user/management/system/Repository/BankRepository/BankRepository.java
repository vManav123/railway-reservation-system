package user.management.system.Repository.BankRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import user.management.system.Models.Bank.Bank_Account;

@Repository("bankRepository")
public interface BankRepository extends MongoRepository<Bank_Account, Long> {
}
