package bank.mangement.service.repository;

import bank.mangement.service.model.bank.Bank_Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("bankRepository")
public interface BankRepository extends MongoRepository<Bank_Account, Long> {
}
