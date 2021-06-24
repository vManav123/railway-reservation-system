package railway.application.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.client.RestTemplate;
import railway.application.system.configuration.securityConfiguration.models.AuthenticationRequest;
import railway.application.system.configuration.securityConfiguration.models.AuthenticationResponse;
import railway.application.system.configuration.securityConfiguration.util.JwtUtil;
import railway.application.system.models.Credentials;
import railway.application.system.models.forms.BankForm;
import railway.application.system.models.forms.ReservationForm;
import railway.application.system.models.forms.UserForm1;
import railway.application.system.models.payment.AddMoney;
import railway.application.system.models.response.*;
import railway.application.system.repository.CredentialsRepository;
import railway.application.system.service.applicationService.ApplicationService;
import railway.application.system.service.emailService.RabbitMQService;
import railway.application.system.service.userDetailsService.MyUserDetailsService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Application")
@Slf4j
@RequestMapping(path = "/application")
public class ApplicationController {


    // *--------------- Autowiring Reference Variables ---------------*
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private MyUserDetailsService userDetailsService;
    private String token;
    // *---------------------------------------------------------------*


    // *--------------- Login Interface -------------------*
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
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

    // *--------------- Decode JWT for Validating User Id with username --------------*
    private Long validateUser() {
        if(token==null)
            return -1L;
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(payload);
        }catch (JSONException err){
            log.info(err.getMessage());
            return -1L;
        }
        String username = jsonObject.getString("sub");
        Credentials credentials;
        if(credentialsRepository.findAll().stream().anyMatch(p -> p.getUsername().equals(username)))
        {
            credentials = credentialsRepository.findAll().stream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
            return credentials.getUser_id();
        }
        else
            return -1L;
    }
    // *---------------------------------------------------*




    @Autowired
    private RabbitMQService rabbitMQSender;
    @Autowired
    private Message message;

    @GetMapping(value = "/producer")
    @ApiIgnore
    public String producer(@RequestParam("message") String message1,@RequestParam("messageId") int messageId) {
        message.setMessage(message1);
        message.setMessageId(messageId);
        rabbitMQSender.send(message);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }




    // *-------------- Public Functionalities -------------*
    @GetMapping(path = "/public/userWelcome")
    @ApiIgnore
    public String UserWelcome() {
        return applicationService.userWelcome();
    }

    @PostMapping("/public/createUserAccount")
    @ApiOperation(value = "User can Create User Account", tags = "User")
    public String createUserAccount(@RequestBody UserForm1 userForm){ log.info("Account Creation in Process");return applicationService.createUserAccount(userForm);}

    @GetMapping("/public/getProfile")
    @ApiOperation(value = "User can get There Profile", tags = "User")
    public ResponseEntity<?> getProfile(long user_id){
        String x = validateLoggedInUser(user_id);
        if (x != null) return ResponseEntity.of(Optional.of(x));
        return ResponseEntity.of(Optional.of(applicationService.getProfile(user_id)));
    }

    // *--------------------------- Get Credentials ------------------------*
    @GetMapping("/public/GetUserCredentials")
    @ApiOperation(value = "User can get Credentials Details", tags = "User")
    public String getCredentialsDetails(long user_id){
        String x = validateLoggedInUser(user_id);
        if (x != null) return x;
        return applicationService.getCredentials(user_id);
    }
    // *--------------------------------------------------------------------*

    private String validateLoggedInUser(long user_id) {
        long userid = validateUser();
        if(userid== user_id)
        {
            log.error("User Doesn't Matched with Logged In User , are You trying to update details of another user ,  please Take Care of this things");
            return "This User Doesn't Exist";
        }
        log.info("Requesting For Credentials is in Process");
        return null;
    }

    @PostMapping("/public/createBankAccount")
    @ApiOperation(value = "User can bank Account", tags = "Bank")
    public String createBankAccount(@RequestBody BankForm bankForm){
        log.info("bank account created");
        return applicationService.createBankAccount(bankForm);
    }

    @PostMapping("/public/addMoneyToBankAccount")
    @ApiOperation(value = "User add money bank Account", tags = "Bank")
    public String addMoney(@RequestBody AddMoney addMoney){return applicationService.addBalance(addMoney);}
    // *-----------------------------------------------------------------------------------*



    // *---------------------------- Train Functionalities --------------------------------*
    @GetMapping(path = "/public/trainDetail/{train_no}")
    @ApiOperation(value = "Display details of trains", notes = "It will display the details of then trains",tags = "Railway")
    public String getTrainDetail(@PathVariable String train_no) {
        log.info("Train Detail Functionality is in Process");
        return applicationService.getTrain(train_no);
    }

    @GetMapping(path = "/public/trainTimeTable/{station}")
    @ApiOperation(value = "Display Time Table of Trains", notes = "It will display the timeTable of all trains from your station",tags = "Railway")
    public String trainTimeTable(@PathVariable String station) {
        log.info("Train TimeTable Functionality is in Process");
        return applicationService.trainTimeTable(station);
    }

    @GetMapping(path = "/public/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations", notes = "It will display all trains between given stations",tags = "Railway")
    public String trainBetweenStation(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train between station is in Process");
        return applicationService.trainBetweenStation(origin, destination);
    }

    @GetMapping(path = "/public/trainFare/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations", notes = "It will display all trains and fare between given stations",tags = "Railway")
    public String trainFares(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train Fare is in Process");
        return applicationService.trainFares(origin, destination);
    }

    @GetMapping(path = "/public/trainLocation")
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations",tags = "Railway")
    public String trainLocation(@RequestBody LocationBody locationBody) {
        log.info("Train Location is in Process");
        return applicationService.trainLocation(locationBody);
    }

    @GetMapping(path = "/public/getTrainByTrainNo/{train_no}")
    @ApiIgnore
    @ApiOperation(value = "Display train Detail to your Train no ", notes = "It will display trains details of given stations",tags = "Railway")
    public ResponseEntity<?> getTrainByTrainNo(@PathVariable String train_no) {
        log.info("Getting Train Details ....");
        return applicationService.getTrainByTrainNo(train_no);
    }
    // *--------------------------------------------------*


    // *------------- Available Accommodation ------------*
    @PostMapping(path = "/public/availableAccommodation")
    @ApiOperation(value = "Display Available Accommodation to your Train ", notes = "It will display Accommodation of given stations",tags = "Railway")
    public String availableAccommodation(@RequestBody AccommodationBody accommodationBody){
        log.info("Available Accommodation is in Process");
        return applicationService.availableAccommodation(accommodationBody);
    }
    // *---------------------------------------------------*


    // *------- Get Reserved Ticket Functionalities -------*
    @GetMapping(path = "/public/checkPNR/{pnr}")
    @ApiOperation(value = "get Train Ticket by your PNR no ", notes = "It will display Train Ticket from given PNR no",tags = "Railway")
    public String getTicket(@PathVariable long pnr) {
        log.info("Checking ticket details by PNR");
        return applicationService.getPNR(pnr);
    }
    // *--------------------------------------------------*


    // *------- Cancellation Ticket Functionalities ------*
    @GetMapping(path = "/nonPublic/cancelTicket/{pnr}")
    @ApiOperation(value = "CancelTrain Ticket by your PNR no ", notes = "It will cancel train ticket from given PNR no",tags = "Railway")
    public String cancelTicket(@PathVariable long pnr) {
        long user_id = validateUser();
        User user = restTemplate.getForObject("http://RAILWAY-API-GATEWAY/user/public/getUser/"+user_id,User.class);
        if(user==null)
        {
            log.error("Ticket Cancellation is in Process");
            return "This User Doesn't Exist";
        }
        if(!user.getTickets().containsKey(pnr))
        {
            log.error("User don't have that ticket");
            return "Your are Not allowed To Book Your Ticket With This User Account , Because the user_id is different from Logged in User";
        }
        log.info("Ticket Cancellation is in Process");
        return applicationService.TicketCancellation(pnr);
    }
    // *--------------------------------------------------*


    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/nonPublic/reserveTicket")
    @ApiOperation(value = "reserve Train Ticket by your Reservation Form ", notes = "It will reserve a Train Ticket from given reservation form",tags = "Railway")
    public String reservedTicket(@RequestBody ReservationForm reservationForm) {
        long user_id = validateUser();
        if(!reservationForm.getUserForm().getUser_id().equals(user_id))
        {
            log.error("User doesn't matched with this User info");
            return "Your are not allowed to book your ticket with this user account , Because the user_id is different from Logged in User";
        }
        log.info("Ticket reservation is in Process");
        return applicationService.reserveTicket(reservationForm);
    }
    // *--------------------------------------------------*


    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/public/availableSeats")
    @ApiOperation(value = "get Available Seat by your PNR no ", notes = "It will get Available Ticket from given PNR no",tags = "Railway")
    public String availableSeats(@RequestBody AvailableSeats availableSeats) {
        log.info("Available Seat Checking is in Process");
        return applicationService.availableSeats(availableSeats); }
    // *--------------------------------------------------*

}
