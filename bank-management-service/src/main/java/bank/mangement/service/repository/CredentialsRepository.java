package bank.mangement.service.repository;

import bank.mangement.service.model.bank.Credentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CredentialsRepository extends MongoRepository<Credentials,String> {
}
