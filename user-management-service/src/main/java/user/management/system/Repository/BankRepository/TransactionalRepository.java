package user.management.system.Repository.BankRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import user.management.system.Models.Bank.TransactionalDetails;
import user.management.system.Models.Bank.TransactionalHistory;

@Repository
public interface TransactionalRepository extends MongoRepository<TransactionalHistory,Long> {
}
