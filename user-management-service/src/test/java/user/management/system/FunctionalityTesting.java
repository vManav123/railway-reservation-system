package user.management.system;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import user.management.system.model.user.ChangePassword;
import user.management.system.model.user.Ticket;
import user.management.system.model.user.User;
import user.management.system.model.user.UserForm;
import user.management.system.service.userService.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@SpringBootTest
@TestComponent
public class FunctionalityTesting {

    @Autowired
    private UserService userService;

    @Test
    public void createUser_TestCase_1(UserForm userForm)
    {
        userForm.setAge(1);
        userForm.setContact_no("1231231");
        userForm.setFull_name("asdds");
        userForm.setEmail_address("Asdasdas");
        userForm.setSecret_key("asda");
        Assertions.assertEquals("!!! Invalid Number !!! , Please take care that the number should be length : 10 and all Digits",userService.createUser(userForm));
    }
    @Test
    public void createUser_TestCase_2(UserForm userForm)
    {
        userForm.setAge(1);
        userForm.setContact_no("1231231aa");
        userForm.setFull_name("asdds");
        userForm.setEmail_address("Asdasdas");
        userForm.setSecret_key("asda");
        Assertions.assertEquals("!!! Invalid Email Address !!! Please Take of this Format email@mail.com",userService.createUser(userForm));
    }
    @Test
    public void createUser_TestCase_3(UserForm userForm)
    {
        userForm.setAge(1);
        userForm.setContact_no("1231231121");
        userForm.setFull_name("asdds");
        userForm.setEmail_address("asdAsdasdas");
        userForm.setSecret_key("asda");
        Assertions.assertEquals("!!! Invalid Email Address !!! Please Take of this Format email@mail.com",userService.createUser(userForm));
    }
    @Test
    public void createUser_TestCase_4(UserForm userForm)
    {
        userForm.setAge(1);
        userForm.setContact_no("1231231121");
        userForm.setFull_name("asdds");
        userForm.setEmail_address("vmanav1999@gmail.com");
        userForm.setSecret_key("asda");
        Assertions.assertEquals("Your User Account has been created and your will get your credentials on email address, go and check it",userService.createUser(userForm));
    }









    @Test
    public void updateUser_TestCase_1(User user)
    {
        user.setUser_id(123L);
        Assertions.assertEquals("Invalid User",userService.updateUser(user));
    }
    @Test
    public void updateUser_TestCase_2(User user)
    {
        user.setUser_id(10001L);
        user.setEmail_address("asdasdas");
        Assertions.assertEquals("Invalid Email",userService.updateUser(user));
    }
    @Test
    public void updateUser_TestCase_3(User user)
    {
        user.setUser_id(10001L);
        user.setEmail_address("vmanav1991@gmail.com");
        user.setAge(-1);
        Assertions.assertEquals("Invalid age",userService.updateUser(user));
    }
    @Test
    public void updateUser_TestCase_4(User user)
    {
        user.setAge(21);
        user.setFull_name("");
        Assertions.assertEquals("Invalid Name",userService.updateUser(user));
    }




    @Test
    public void ChangePassword_TestCase_1(ChangePassword changePassword)
    {
        changePassword.setUser_id(1000000);
        Assertions.assertEquals("!!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id",userService.changePassword(changePassword));
    }
    @Test
    public void ChangePassword_TestCase_2(ChangePassword changePassword)
    {
        changePassword.setUser_id(10001);
        changePassword.setNew_password("hello");
        changePassword.setConfirm_password("Hello");
        Assertions.assertEquals("Password not matching",userService.changePassword(changePassword));
    }
    @Test
    public void ChangePassword_TestCase_3(ChangePassword changePassword)
    {
        changePassword.setUser_id(10001);
        changePassword.setNew_password("hello");
        changePassword.setConfirm_password("hello");
        Assertions.assertEquals("Your password Changed Successfully for this user_id : 10001",userService.changePassword(changePassword));
    }
    @Test
    public void ChangePassword_TestCase_4(User user)
    {
        user.setUser_id(10001L);
        user.setEmail_address("asdasdas");
        Assertions.assertEquals("Invalid Email",userService.updateUser(user));
    }


    @Test
    public void saveTicket_TestCase_1(Ticket ticket)
    {
        Assertions.assertEquals("!!! There is no User Exist with this ID !!!       ----> Kindly Please Check the User Id",userService.saveTicket(1212L,2,ticket));
    }
    @Test
    public void saveTicket_TestCase_2(Ticket ticket)
    {
        Assertions.assertEquals("Invalid PNR",userService.saveTicket(10001L,-1,ticket));
    }
    @Test
    public void saveTicket_TestCase_3(Ticket ticket)
    {
        Assertions.assertEquals("Invalid PNR",userService.saveTicket(10001L,-1,ticket));
    }
    @Test
    public void saveTicket_TestCase_4(Ticket ticket)
    {
        Assertions.assertEquals("Invalid PNR",userService.saveTicket(10001L,-1,ticket));
    }




    @Test
    public void getUser_TestCase_1()
    {
        Assertions.assertEquals((new User(0L,"no User Exist",0,"null","null","null",0L,"null","null", LocalDate.now(),"null",1,false, LocalTime.MIN,new HashMap<>())).getUser_id(),userService.getUser(123123).getUser_id());
    }
    @Test
    public void getUser_TestCase_2()
    {
        Assertions.assertEquals((new User(0L,"no User Exist",0,"null","null","null",0L,"null","null", LocalDate.now(),"null",1,false, LocalTime.MIN,new HashMap<>())).getBank_name(),userService.getUser(123113).getBank_name());
    }
    @Test
    public void getUser_TestCase_3()
    {
        Assertions.assertEquals((new User(0L,"no User Exist",0,"null","null","null",0L,"null","null", LocalDate.now(),"null",1,false, LocalTime.MIN,new HashMap<>())).getAge(),userService.getUser(12223).getAge());
    }
    @Test
    public void getUser_TestCase_4()
    {
        Assertions.assertEquals((new User(0L,"no User Exist",0,"null","null","null",0L,"null","null", LocalDate.now(),"null",1,false, LocalTime.MIN,new HashMap<>())).getAccount_no(),userService.getUser(23123).getAccount_no());
    }




}
