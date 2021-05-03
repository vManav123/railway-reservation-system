package user.management.system.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.Models.User.User;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;
import user.management.system.Service.SequenceGenerator.UserSequenceGeneratorService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;

    @Autowired
    private UserSequenceGeneratorService sequenceGeneratorService;

    public List<User> getUser()
    {
        return userRepository.findAll();
    }
    @Override
    public String addUser(User user)
    {
        user.setUser_id(sequenceGeneratorService.getSequenceNmber("user_sequence"));
        return "*--------- User added Successfully ---------*";
    }

}
