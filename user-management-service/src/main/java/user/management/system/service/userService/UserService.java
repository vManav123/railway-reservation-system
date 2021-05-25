package user.management.system.service.userService;

import org.springframework.stereotype.Service;
import user.management.system.model.user.*;

import java.util.List;

@Service
public interface UserService {
    public String addUser(User user);

    public String addAllUser(List<User> users);

    public List<User> getAllUser();

    public String createUser(UserForm1 userForm1);

    public boolean userExistById(Long user_id);

    public String updateUser(User user);

    public User getUser(long user_id);

    public String changePassword(ChangePassword changePassword);

    public String saveTicket(Long account_no, long pnr, Ticket ticket);

    public Credentials getCredentials(long user_id);
}
