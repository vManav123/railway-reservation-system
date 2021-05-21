package user.management.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import user.management.system.model.user.ChangePassword;
import user.management.system.model.user.Ticket;
import user.management.system.model.user.User;
import user.management.system.model.user.UserForm1;

@SpringBootTest
@Import({FunctionalityTesting.class})
class MainTests {

    @Autowired
    private FunctionalityTesting functionalityTesting;
    private UserForm1 userForm1;
    private User user;
    private ChangePassword changePassword;
    private Ticket ticket;



    @Test
    public void CreateUser_Testing()
    {
        userForm1 =new UserForm1();
        functionalityTesting.createUser_TestCase_1(userForm1);
        functionalityTesting.createUser_TestCase_2(userForm1);
        functionalityTesting.createUser_TestCase_3(userForm1);
        functionalityTesting.createUser_TestCase_4(userForm1);
    }
    @Test
    public void updateUser_Testing()
    {
        user=new User();
        functionalityTesting.updateUser_TestCase_1(user);
        functionalityTesting.updateUser_TestCase_2(user);
        functionalityTesting.updateUser_TestCase_3(user);
        functionalityTesting.updateUser_TestCase_4(user);
    }
    @Test
    public void ChangePassword_Testing()
    {
        user=new User();
        changePassword=new ChangePassword();
        functionalityTesting.ChangePassword_TestCase_1(changePassword);
        functionalityTesting.ChangePassword_TestCase_2(changePassword);
        functionalityTesting.ChangePassword_TestCase_3(changePassword);
        functionalityTesting.ChangePassword_TestCase_4(user);
    }
    @Test
    public void saveTicket_Testing()
    {
        ticket=new Ticket();
        functionalityTesting.saveTicket_TestCase_1(ticket);
        functionalityTesting.saveTicket_TestCase_2(ticket);
        functionalityTesting.saveTicket_TestCase_3(ticket);
        functionalityTesting.saveTicket_TestCase_4(ticket);
    }
    @Test
    public void AddUser_Testing()
    {
        functionalityTesting.getUser_TestCase_1();
        functionalityTesting.getUser_TestCase_2();
        functionalityTesting.getUser_TestCase_3();
        functionalityTesting.getUser_TestCase_4();
    }
}
