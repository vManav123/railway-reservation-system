package user.management.system.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.Models.User;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;

import java.util.List;

@Service
public interface UserService {
    public String addUser(User user);
}
