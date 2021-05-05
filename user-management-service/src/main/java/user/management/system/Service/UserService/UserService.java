package user.management.system.Service.UserService;

import org.springframework.stereotype.Service;
import user.management.system.Models.User.User;
import user.management.system.Models.User.UserForm;

import java.util.List;

@Service
public interface UserService {
    public String addUser(User user);
    public String addAllUser(List<User> users);
    public List<User> getAllUser();
    public String createUser(UserForm userForm);
}
