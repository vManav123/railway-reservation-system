package railway.application.system.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import railway.application.system.Configuration.SecurityConfiguration.models.AuthenticationRequest;
import railway.application.system.Configuration.SecurityConfiguration.models.AuthenticationResponse;
import railway.application.system.Configuration.SecurityConfiguration.util.JwtUtil;
import railway.application.system.Models.Forms.ReservationForm;
import railway.application.system.Service.ApplicationService.ApplicationService;
import railway.application.system.Service.UserDetailsService.MyUserDetailsService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "Application")
@RequestMapping(path = "/application")
public class ApplicationController {


    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;


    // *--------------- Login Interface -------------------*
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    // *---------------------------------------------------*


    // *-------------- Public Functionalities -------------*

//    @ApiOperation(
//            value = "Get All",
//            produces = APPLICATION_JSON_VALUE
//            , authorizations = {@Authorization(value = SwaggerConfiguration.securitySchemaOAuth2, scopes =
//            {@AuthorizationScope(scope = SwaggerConfiguration.authorizationScopeGlobal, description = SwaggerConfiguration.authorizationScopeGlobalDesc)})}
//    )

    @GetMapping(path = "/userWelcome")
    @ApiIgnore
    public String UserWelcome() {
        return applicationService.userWelcome();
    }

    @GetMapping(path = "/trainDetail/{train_no}")
    @ApiOperation(value = "Display details of trains", notes = "It will display the details of then trains")
    public String getTrainDetail(@PathVariable String train_no) {
        return applicationService.getTrain(train_no);
    }

    @GetMapping(path = "/trainTimeTable/{station}")
    @ApiOperation(value = "Display Time Table of Trains", notes = "It will display the timeTable of all trains from your station")
    public String trainTimeTable(@PathVariable String station) {
        return applicationService.trainTimeTable(station);
    }

    @GetMapping(path = "/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations", notes = "It will display all trains between given stations")
    public String trainBetweenStation(@PathVariable String origin, @PathVariable String destination) {
        return applicationService.trainBetweenStation(origin, destination);
    }

    @GetMapping(path = "/trainFare/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations", notes = "It will display all trains and fare between given stations")
    public String trainFares(@PathVariable String origin, @PathVariable String destination) {
        return applicationService.trainFares(origin, destination);
    }

    @GetMapping(path = "/trainLocation/{train_info}:{your_location}:{day}")
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations")
    public String trainLocation(@PathVariable String train_info, @PathVariable String your_location, @PathVariable String day) {
        return applicationService.trainLocation(train_info, your_location, day);
    }
    // *---------------------------------------------------*


    // *------- Get Reserved Ticket Functionalities -------*
    @GetMapping(path = "/checkPNR/{pnr}")
    public String getTicket(@PathVariable long pnr) {
        return applicationService.getPNR(pnr);
    }
    // *--------------------------------------------------*


    // *------- Cancellation Ticket Functionalities ------*
    @GetMapping(path = "/cancelTicket/{pnr}")
    public String cancelTicket(@PathVariable long pnr) {
        return applicationService.TicketCancellation(pnr);
    }
    // *--------------------------------------------------*


    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/reserveTicket")
    public String reservedTicket(@RequestBody ReservationForm reservationForm) {
        return applicationService.reserveTicket(reservationForm);
    }
    // *--------------------------------------------------*

}
