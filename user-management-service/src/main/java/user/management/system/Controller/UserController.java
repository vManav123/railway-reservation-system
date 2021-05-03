package user.management.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import user.management.system.Models.User.User;
import user.management.system.Service.UserService.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    // *-----------------  Basic Functionality -----------------*
    @PostMapping(path = "/addUser")

    public String addUser(@RequestBody User user) { return userService.addUser(user); }
    // *--------------------------------------------------------*

}
