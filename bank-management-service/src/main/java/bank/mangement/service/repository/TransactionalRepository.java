package bank.mangement.service.repository;

import bank.mangement.service.model.bank.TransactionalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionalRepository extends MongoRepository<TransactionalHistory,Long> {

}
