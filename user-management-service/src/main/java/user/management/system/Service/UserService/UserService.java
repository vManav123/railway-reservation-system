package user.management.system.Service.UserService;

import org.springframework.stereotype.Service;
import user.management.system.Models.User.User;

@Service
public interface UserService {
    public String addUser(User user);
}
