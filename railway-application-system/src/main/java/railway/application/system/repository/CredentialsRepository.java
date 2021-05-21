package railway.application.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import railway.application.system.models.Credentials;

public interface CredentialsRepository extends MongoRepository<Credentials,String> {
    public Credentials findCredentialsByUsername(String username);
}
