package user.management.system.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.ExceptionHandling.InvalidContactNumberException;
import user.management.system.ExceptionHandling.UserNotExistException;
import user.management.system.Models.User.User;
import user.management.system.Models.User.UserForm;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;
import user.management.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    // *---------------- Autowired Reference Variables ---------------*
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;

    @Autowired
    private DataSequenceGeneratorService sequenceGeneratorService;

    @Autowired
    User user;
    // *-----------------------------------------------------------------*


    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "      !!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Number !!! , Please take care that the number should be length : 10 and all Digits";
    // *-----------------------------------------------------------------*


    @Override
    public List<User> getAllUser()
    {
        return userRepository.findAll();
    }

    public boolean isNumeric(String info)
    {
        try {
            Double.parseDouble(info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    // *----------------------- Account Creation Functionalities ---------------------- *
    @Override
    public String createUser(UserForm userForm) {
        try {
            if(userForm.getContact_no().length()!=10 && isNumeric(userForm.getContact_no()))
                throw new InvalidContactNumberException(invalidContactNumberException);
        }
        catch (InvalidContactNumberException e)
        {
            return e.getMessage();
        }
        user.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence"));
        user.setFull_name(userForm.getFull_name());
        user.setAge(userForm.getAge());
        user.setContact_no(userForm.getContact_no());
        user.setUsername("user"+Arrays.stream(userForm.getFull_name().split(" ")).toList().get(0));
        String password = "";
        for(int i = 0; i < 8+new Random().nextInt(2); i++)
        {
            password += (char) (new Random().nextInt(62)+64);
        }
        user.setPassword(password);
        user.setRoles("USER");
        userRepository.save(user);
        return "User Created Successfully \n\n Your Credentials are here \n Username : "+user.getUsername()+" \n Password : "+user.getPassword();
    }
    // *---------------------------------------------------------------------------------*



    // *------------------------------- Basic Functionalities ---------------------------*
    @Override
    public String addUser(User user)
    {
        user.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence"));
        return "*--------- User added Successfully ---------*";
    }

    @Override
    public String addAllUser(List<User> users) {
        users.forEach( p -> p.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence")));
        userRepository.saveAll(users);
        return "*------ All Users Added Successfully -------*";
    }
    // *---------------------------------------------------------------------------------*


}
