package railway.application.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import railway.application.system.configuration.securityConfiguration.models.AuthenticationRequest;
import railway.application.system.configuration.securityConfiguration.models.AuthenticationResponse;
import railway.application.system.configuration.securityConfiguration.util.JwtUtil;
import railway.application.system.models.forms.BankForm;
import railway.application.system.models.forms.ReservationForm;
import railway.application.system.models.forms.UserForm;
import railway.application.system.models.forms.UserForm1;
import railway.application.system.models.payment.AddMoney;
import railway.application.system.models.response.AccommodationBody;
import railway.application.system.models.response.AvailableSeats;
import railway.application.system.models.response.LocationBody;
import railway.application.system.models.response.Message;
import railway.application.system.service.userDetailsService.MyUserDetailsService;
import railway.application.system.service.applicationService.ApplicationService;
import railway.application.system.service.emailService.RabbitMQService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

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
    private MyUserDetailsService userDetailsService;
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
        log.info("token is generated");
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
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

    @GetMapping(path = "/users/userWelcome")
    @ApiIgnore
    public String UserWelcome() {
        return applicationService.userWelcome();
    }

    @PostMapping("/train/createUserAccount")
    public String createUserAccount(@RequestBody UserForm1 userForm){ log.info("Account Creation in Process");return applicationService.createUserAccount(userForm);}

    @PostMapping("/train/createBankAccount")
    public String createBankAccount(@RequestBody BankForm bankForm){
        log.info("bank account created");
        return applicationService.createBankAccount(bankForm);
    }

    @PostMapping("/train/addMoneyToBankAccount")
    public String addMoney(@RequestBody AddMoney addMoney){return applicationService.addBalance(addMoney);}

    @GetMapping(path = "/train/trainDetail/{train_no}")
    @ApiOperation(value = "Display details of trains", notes = "It will display the details of then trains")
    public String getTrainDetail(@PathVariable String train_no) {
        log.info("Train Detail Functionality is in Process");
        return applicationService.getTrain(train_no);
    }

    @GetMapping(path = "/train/trainTimeTable/{station}")
    @ApiOperation(value = "Display Time Table of Trains", notes = "It will display the timeTable of all trains from your station")
    public String trainTimeTable(@PathVariable String station) {
        log.info("Train TimeTable Functionality is in Process");
        return applicationService.trainTimeTable(station);
    }

    @GetMapping(path = "/train/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations", notes = "It will display all trains between given stations")
    public String trainBetweenStation(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train between station is in Process");
        return applicationService.trainBetweenStation(origin, destination);
    }

    @GetMapping(path = "/train/trainFare/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations", notes = "It will display all trains and fare between given stations")
    public String trainFares(@PathVariable String origin, @PathVariable String destination) {
        log.info("Train Fare is in Process");
        return applicationService.trainFares(origin, destination);
    }

    @GetMapping(path = "/train/trainLocation")
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations")
    public String trainLocation(@RequestBody LocationBody locationBody) {
        log.info("Train Location is in Process");
        return applicationService.trainLocation(locationBody);
    }

    @GetMapping(path = "/train/getTrainByTrainNo/{train_no}")
    @ApiIgnore
    public ResponseEntity<?> getTrainByTrainNo(@PathVariable String train_no) {
        log.info("Getting Train Details ....");
        return applicationService.getTrainByTrainNo(train_no);
    }
    // *--------------------------------------------------*


    // *------------- Available Accommodation ------------*
    @PostMapping(path = "/train/availableAccommodation")
    public String availableAccommodation(@RequestBody AccommodationBody accommodationBody){
        log.info("Available Accommodation is in Process");
        return applicationService.availableAccommodation(accommodationBody);
    }
    // *---------------------------------------------------*


    // *------- Get Reserved Ticket Functionalities -------*
    @GetMapping(path = "/train/checkPNR/{pnr}")
    public String getTicket(@PathVariable long pnr) {
        log.info("Checking ticket details by PNR");
        return applicationService.getPNR(pnr);
    }
    // *--------------------------------------------------*


    // *------- Cancellation Ticket Functionalities ------*
    @GetMapping(path = "/reserve/cancelTicket/{pnr}")
    public String cancelTicket(@PathVariable long pnr) {
        log.info("Ticket Cancellation is in Process");
        return applicationService.TicketCancellation(pnr);
    }
    // *--------------------------------------------------*


    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/reserve/reserveTicket")
    public String reservedTicket(@RequestBody ReservationForm reservationForm) {
        log.info("Ticket reservation is in Process");
        return applicationService.reserveTicket(reservationForm);
    }
    // *--------------------------------------------------*


    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/train/availableSeats")
    public String availableSeats(@RequestBody AvailableSeats availableSeats) { return applicationService.availableSeats(availableSeats); }
    // *--------------------------------------------------*

}
