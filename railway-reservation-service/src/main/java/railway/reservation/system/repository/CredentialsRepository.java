package railway.reservation.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import railway.reservation.system.model.controllerBody.Credentials;

public interface CredentialsRepository extends MongoRepository<Credentials,String> {
    public Credentials findCredentialsByUsername(String username);
}
