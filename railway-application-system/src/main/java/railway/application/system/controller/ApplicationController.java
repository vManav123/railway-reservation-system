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


    private Long validateUser() {
        if(token==null)
            return -1L;
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
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
    public String createUserAccount(@RequestBody UserForm1 userForm){ log.info("Account Creation in Process");return applicationService.createUserAccount(userForm);}

    @PostMapping("/public/createBankAccount")
    public String createBankAccount(@RequestBody BankForm bankForm){
        log.info("bank account created");
        return applicationService.createBankAccount(bankForm);
    }

    @PostMapping("/public/addMoneyToBankAccount")
    public String addMoney(@RequestBody AddMoney addMoney){return applicationService.addBalance(addMoney);}

    @GetMapping(path = "/public/trainDetail/{train_no}")
    @ApiOperation(value = "Display details of trains", notes = "It will display the details of then trains")
    public String getTrainDetail(@PathVariable String train_no) {
        log.info("Train Detail Functionality is in Process");
        return applicationService.getTrain(train_no);
    }

    @GetMapping(path = "/public/trainTimeTable/{station}")
    @ApiOperation(value = "Display Time Table of Trains", notes = "It will display the timeTable of all trains from your station")
    public String trainTimeTable(@PathVariable String station) {
        log.info("Train TimeTable Functionality is in Process");
        return applicationService.trainTimeTable(station);
    }

    @GetMapping(path = "/public/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations", notes = "It will display all trains between given stations")
    public String trainBetweenStation(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train between station is in Process");
        return applicationService.trainBetweenStation(origin, destination);
    }

    @GetMapping(path = "/public/trainFare/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations", notes = "It will display all trains and fare between given stations")
    public String trainFares(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train Fare is in Process");
        return applicationService.trainFares(origin, destination);
    }

    @GetMapping(path = "/public/trainLocation")
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations")
    public String trainLocation(@RequestBody LocationBody locationBody) {
        log.info("Train Location is in Process");
        return applicationService.trainLocation(locationBody);
    }

    @GetMapping(path = "/public/getTrainByTrainNo/{train_no}")
    @ApiIgnore
    public ResponseEntity<?> getTrainByTrainNo(@PathVariable String train_no) {
        log.info("Getting Train Details ....");
        return applicationService.getTrainByTrainNo(train_no);
    }
    // *--------------------------------------------------*


    // *------------- Available Accommodation ------------*
    @PostMapping(path = "/public/availableAccommodation")
    public String availableAccommodation(@RequestBody AccommodationBody accommodationBody){
        log.info("Available Accommodation is in Process");
        return applicationService.availableAccommodation(accommodationBody);
    }
    // *---------------------------------------------------*


    // *------- Get Reserved Ticket Functionalities -------*
    @GetMapping(path = "/public/checkPNR/{pnr}")
    public String getTicket(@PathVariable long pnr) {
        log.info("Checking ticket details by PNR");
        return applicationService.getPNR(pnr);
    }
    // *--------------------------------------------------*


    // *------- Cancellation Ticket Functionalities ------*
    @GetMapping(path = "/nonPublic/cancelTicket/{pnr}")
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
    public String availableSeats(@RequestBody AvailableSeats availableSeats) {

        return applicationService.availableSeats(availableSeats); }
    // *--------------------------------------------------*

}
