package user.management.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.Models.User.User;
import user.management.system.Models.User.UserForm;
import user.management.system.Service.UserService.UserService;

import java.util.List;


@RestController("userController")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    // *-----------------  Basic Functionality -----------------*
    @GetMapping(path = "/welcome")
    public String user(){return "Welcome to User Interface";}

    @PostMapping(path = "/addUser")
    public String addUser(@RequestBody User user) { return userService.addUser(user); }

    @PostMapping(path = "/addAllUser")
    public String addAllUser(@RequestBody List<User> users){return userService.addAllUser(users);}

    @PostMapping(path = "/createUser")
    public String createUser(@RequestBody UserForm userForm){return userService.createUser(userForm);}
    // *--------------------------------------------------------*

}
