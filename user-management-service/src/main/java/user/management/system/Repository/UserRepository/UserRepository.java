package user.management.system.Repository.UserRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import user.management.system.Models.User.User;

@Repository("userRepository")
public interface UserRepository extends MongoRepository<User,Long> {
}
