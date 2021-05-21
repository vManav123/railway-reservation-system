package user.management.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import user.management.system.model.user.User;


@Repository("userRepository")
public interface UserRepository extends MongoRepository<User, Long> {
}
