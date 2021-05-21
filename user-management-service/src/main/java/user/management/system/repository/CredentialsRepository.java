package user.management.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import user.management.system.model.user.Credentials;


@Repository
public interface CredentialsRepository extends MongoRepository<Credentials,String> {
}
