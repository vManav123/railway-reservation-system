package user.management.system.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import user.management.system.ExceptionHandling.InvalidContactNumberException;
import user.management.system.ExceptionHandling.InvalidEmailException;
import user.management.system.Models.User.User;
import user.management.system.Models.User.UserForm;
import user.management.system.Repository.BankRepository.BankRepository;
import user.management.system.Repository.UserRepository.UserRepository;
import user.management.system.Service.SequenceGenerator.DataSequenceGeneratorService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    User user;
    // *------------------------ Exception Messages ---------------------*
    String bankNotExistException = " !!! There is no Bank Exist from this name Kindly Check the name that are listed here !!!";
    String userNotExistException = "      !!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id";
    String invalidContactNumberException = "!!! Invalid Number !!! , Please take care that the number should be length : 10 and all Digits";
    // *-----------------------------------------------------------------*
    String invalidEmailException = "!!! Invalid Email Address !!! Please Take of this Format email@mail.com";
    // *---------------- Autowired Reference Variables ---------------*
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;
    @Autowired
    @Qualifier("bankRepository")
    private BankRepository bankRepository;
    @Autowired
    private DataSequenceGeneratorService sequenceGeneratorService;
    // *-----------------------------------------------------------------*

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public boolean isNumeric(String info) {
        try {
            Double.parseDouble(info);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    // *-------------User Account Creation Functionalities -------------*
    public boolean isEmailValid(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public String createUser(UserForm userForm) {
        try {
            if (userForm.getContact_no().length() != 10 && isNumeric(userForm.getContact_no()))
                throw new InvalidContactNumberException(invalidContactNumberException);
            if (!isEmailValid(userForm.getEmail_address()))
                throw new InvalidEmailException(invalidEmailException);
            InternetAddress internetAddress = new InternetAddress(userForm.getEmail_address());
            internetAddress.validate();
        } catch (InvalidContactNumberException | InvalidEmailException | AddressException e) {
            return e.getMessage();
        }


        user.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence"));
        user.setFull_name(userForm.getFull_name());
        user.setAge(userForm.getAge());
        user.setContact_no(userForm.getContact_no());
        user.setUsername("user" + Arrays.stream(userForm.getFull_name().split(" ")).toList().get(0));
        user.setEmail_address(userForm.getEmail_address());
        // Generating password here
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8 + new Random().nextInt(5); i++) {
            password.append((char) (new Random().nextInt(62) + 64));
        }
        user.setBank_name("no Bank added");
        user.setAccount_no(0L);
        user.setCredit_card_no("null");
        user.setCvv("null");
        user.setExpiry_date(new Date());
        user.setFailed_attempts(0);
        user.setAccount_non_locked(false);
        user.setLock_time(LocalTime.MIN);
        user.setTickets(new HashMap<>());
        user.setPassword(password.toString());
        user.setRoles("ADMIN");
        userRepository.save(user);
        return "User Created Successfully \n\n Your Credentials are here \n Username : " + user.getUsername() + " \n Password : " + user.getPassword();
    }
    // *---------------------------------------------------------------------------------*


    // *------------------------------- Basic Functionalities ---------------------------*
    @Override
    public String addUser(User user) {
        user.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence"));
        return "*--------- User added Successfully ---------*";
    }

    @Override
    public boolean userExistById(Long user_id) {
        return userRepository.existsById(user_id);
    }

    @Override
    public String addAllUser(List<User> users) {
        users.forEach(p -> p.setUser_id(sequenceGeneratorService.getUserSequenceNmber("user_sequence")));
        userRepository.saveAll(users);
        return "*------ All Users Added Successfully -------*";
    }
    // *---------------------------------------------------------------------------------*


}
