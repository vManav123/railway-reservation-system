package user.management.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import user.management.system.configuration.security.models.AuthenticationRequest;
import user.management.system.configuration.security.models.AuthenticationResponse;
import user.management.system.configuration.security.util.JwtUtil;
import user.management.system.model.user.ChangePassword;
import user.management.system.model.user.Ticket;
import user.management.system.model.user.User;
import user.management.system.model.user.UserForm1;
import user.management.system.repository.CredentialsRepository;
import user.management.system.service.userDetailsService.MyUserDetailsService;
import user.management.system.service.userService.UserService;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController("userController")
@RequestMapping("/user")
@Slf4j
public class UserController {

    // *---------------------------------- User Functionalities -------------------------------*


    // *--------- Autowiring Reference Variables --------*
    @Autowired
    private UserService userService;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;
    private String token = "";
    // *---------------------------------------------------------------*


    // *--------------- Login Interface -------------------*
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        log.info("Authentication is in Process");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            log.info("Authentication is failed");
            return ResponseEntity.of(Optional.of("Wrong Username and Password , Please Type it correctly"));
        }
        log.info("Authentication is done");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        token = jwt;
        log.info("token is generated");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    // *---------------------------------------------------*


    // *--------------  Basic Functionality ---------------*
    @GetMapping(path = "/public/welcome")
    @ApiIgnore
    public String user() {
        return "Welcome to User Interface";
    }

    @GetMapping(path = "/public/getEmail/{id}")
    public String getEmailAddress(@PathVariable long id){return userService.getEmailAddress(id);}

    @PostMapping(path = "/nonPublic/addUser")
    public String addUser(@RequestBody User user)    {
        return userService.addUser(user);
    }

    @PostMapping(path = "/nonPublic/addAllUser")
    public String addAllUser(@RequestBody List<User> users) {
        return userService.addAllUser(users);
    }

    @PostMapping(path = "/public/createUser")
    public String createUser(@RequestBody UserForm1 userForm1) {
        return userService.createUser(userForm1);
    }

    @GetMapping(path = "/public/getAllUsers")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "/public/userExistById/{user_id}")
    public boolean userExistById(@PathVariable Long user_id) {
        return userService.userExistById(user_id);
    }

    @PostMapping(path = "/public/updateUser")
    public String updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

    @GetMapping(path = "/public/userCredential/{user_id}")
    public String getCredentials(@PathVariable long user_id){return userService.getCredentials(user_id);}

    private Long validateUser() {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        System.out.println(payload);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(payload);
        }catch (JSONException err){
            log.info(err.getMessage());
            return -1L;
        }
        String username = jsonObject.getString("sub");
        return credentialsRepository.findAll().stream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0).getUser_id();
    }

    @GetMapping(path = "/public/getUser/{user_id}")
    public User getUser(@PathVariable long user_id){return userService.getUser(user_id);}

    @PostMapping("/public/changePassword")
    public String changePassword(@RequestBody ChangePassword changePassword){return userService.changePassword(changePassword);}

    @PostMapping(path = "/public/saveTicket/{account_no}:{pnr}")
    @ApiIgnore
    public String saveTicket(@PathVariable long account_no,@PathVariable long pnr , @RequestBody Ticket ticket){return userService.saveTicket(account_no,pnr,ticket);}
    // *----------------------------------------------------*


    // *------------------------------------------ End of User Functionalities -----------------------------------------*


}