package user.management.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.model.user.ChangePassword;
import user.management.system.model.user.Ticket;
import user.management.system.model.user.User;
import user.management.system.model.user.UserForm;
import user.management.system.service.userService.UserService;


import java.util.List;


@RestController("userController")
@RequestMapping("/user")
public class UserController {

    // *---------------------------------- User Functionalities -------------------------------*


    // *--------- Autowiring Reference Variables --------*
    @Autowired
    private UserService userService;
    //  *------------------------------------------------*




    // *--------------  Basic Functionality ---------------*
    @GetMapping(path = "/welcome")
    public String user() {
        return "Welcome to User Interface";
    }

    @PostMapping(path = "/addUser")
    public String addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PostMapping(path = "/addAllUser")
    public String addAllUser(@RequestBody List<User> users) {
        return userService.addAllUser(users);
    }

    @PostMapping(path = "/createUser")
    public String createUser(@RequestBody UserForm userForm) {
        return userService.createUser(userForm);
    }

    @GetMapping(path = "/getAllUsers")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "/userExistById/{user_id}")
    public boolean userExistById(@PathVariable Long user_id) {
        return userService.userExistById(user_id);
    }

    @PostMapping(path = "/updateUser")
    public String updateUser(@RequestBody User user){return userService.updateUser(user);}

    @GetMapping(path = "/getUser/{user_id}")
    public User getUser(@PathVariable long user_id){return userService.getUser(user_id);}

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody ChangePassword changePassword){return userService.changePassword(changePassword);}

    @PostMapping(path = "/saveTicket/{account_no}:{pnr}")
    public String saveTicket(@PathVariable long account_no,@PathVariable long pnr , @RequestBody Ticket ticket){return userService.saveTicket(account_no,pnr,ticket);}
    // *----------------------------------------------------*


    // *------------------------------------------ End of User Functionalities -----------------------------------------*


}