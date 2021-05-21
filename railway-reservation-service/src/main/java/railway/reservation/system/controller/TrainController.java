package railway.reservation.system.controller;

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
import railway.reservation.system.configuration.security.models.AuthenticationRequest;
import railway.reservation.system.configuration.security.models.AuthenticationResponse;
import railway.reservation.system.configuration.security.util.JwtUtil;
import railway.reservation.system.model.TimeTable;
import railway.reservation.system.model.TrainLocation;
import railway.reservation.system.model.TrainsBetweenStation;
import railway.reservation.system.model.controllerBody.AccommodationBody;
import railway.reservation.system.model.controllerBody.AvailableSeats;
import railway.reservation.system.model.controllerBody.LocationBody;
import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.model.ticket.SeatData;
import railway.reservation.system.model.ticket.Ticket;
import railway.reservation.system.model.ticket.TicketStatus;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.service.reservationService.ReservationService;
import railway.reservation.system.service.trainSeatService.TrainSeatService;
import railway.reservation.system.service.trainService.TrainService;
import railway.reservation.system.service.userDetailsService.MyUserDetailsService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;


@RestController
@Slf4j
@RequestMapping(path = "/trains")
public class TrainController {



    // *-------- Train Service Autowiring -------*
    @Autowired
    TrainService trainService;
    @Autowired
    private TrainSeatService trainSeatService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;
    // *---------------------------------------------*


    // *-------------- Login Interface ---------------*
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




    // *--------------------------------------- Ticket Reservation Functionality ---------------------------------------------*
    @GetMapping(path = "/addData")
    @ApiIgnore
    public String addData() {
        log.trace("Added Data is Accessed");
        return trainSeatService.addData();
    }

    @GetMapping(path = "/welcome")
    @ApiIgnore
    public String welcome() {
        return "Welcome to Pakistan Railway Reservation portal";
    }

    @PostMapping(path = "/reserveTicket")
    @ApiIgnore
    public TicketStatus reserveTicket(@RequestBody Ticket ticket){return reservationService.reserveTicket(ticket);}

    @PostMapping(path = "/reservedTicket")
    @ApiIgnore
    public String reservedTicket(@RequestBody ReservedTicket reservedTicket){return reservationService.reservedTicket(reservedTicket);}

    @GetMapping(path = "/ticketExistByPNR/{pnr}")
    @ApiIgnore
    public boolean chkPNR(@PathVariable long pnr){return reservationService.ticketExistByPNR(pnr);}

    @GetMapping(path = "/getTicket/{pnr}")
    @ApiIgnore
    public ReservedTicket getTicket(@PathVariable long pnr){return reservationService.getTicket(pnr);}

    @GetMapping(path = "/cancelTicket/{pnr}")
    @ApiIgnore
    public String cancelTicket(@PathVariable long pnr){return reservationService.ticketCancellation(pnr);}

    @PostMapping(path = "/cancelSeat")
    @ApiIgnore
    public String cancelSeat(@RequestBody SeatData seatData){return reservationService.seatCancellation(seatData.getSeat_no(),seatData.getClass_name(),seatData.getSeat_id());}

    @PostMapping(path = "/availableSeats")
    @ApiIgnore
    public String availableSeats(@RequestBody AvailableSeats availableSeats){return reservationService.availableSeats(availableSeats.getStart(),availableSeats.getDestination(),availableSeats.getLocalDate());}

    @PostMapping(path = "/availableAccommodation")
    @ApiIgnore
    public String availableAccommodation(@RequestBody AccommodationBody accommodationBody){return reservationService.availableAccommodation(accommodationBody.getTrain_no(),accommodationBody.getDate());}
    // *---------------------------------------------- End of Reservation Functionalities ------------------------------------*



    // *----------------------------------------------- Train Management Functionalities -------------------------------------*

    //  *-------- Time Table Functionality -------*
    @GetMapping(path = "/timeTable/{city}")
    @ApiIgnore
    @ApiOperation(value = "get Time Table of Trains", notes = "It Will Display the TimeTable of All Trains From Your Station in JSON format", response = TimeTable.class)
    public List<TimeTable> displayTimeTable(@PathVariable String city) {
        return trainService.displayTimeTable(city);
    }

    @GetMapping(path = "/trainTimeTable/{city_name}")
    @ApiOperation(value = "Display Time Table of Trains", notes = "It Will Display the TimeTable of All Trains From Your Station into Table Format ", response = TimeTable.class)
    public String displayTimeToTable(@PathVariable String city_name) { return trainService.displayTimeTableByYourCity(city_name); }
    // *-------------------------------------------*


    // *---Train Between Stations Functionality ---*
    @GetMapping(path = "/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations", notes = "It will display all trains between given stations into Table Format", response = TrainsBetweenStation.class)
    public String getTrainsBetweenStationToTable(@PathVariable String origin, @PathVariable String destination) { return trainService.trainsBetweenStationToTable(origin, destination); }

    @GetMapping(path = "/trainsBetweenStations/{origin}:{destination}")
    @ApiIgnore
    @ApiOperation(value = "get trains between stations", notes = "It will display all trains     between given stations into JSON Format", response = TrainsBetweenStation.class)
    public List<TrainsBetweenStation> getTrainsBetweenStation(@PathVariable String origin, @PathVariable String destination) { return trainService.trainsBetweenStation(origin, destination); }
    // *----------------------------------- ---------*


    // *------- Train Location Functionality -------*
    @PostMapping(path = "/trainsLocation")
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations into JSON Format", response = TrainsBetweenStation.class)
    public String getTrainLocationToTable(@RequestBody LocationBody locationBody) {
        return trainService.trainLocationToTable(locationBody.getTrain_no(),locationBody.getDate(),locationBody.getYour_location());
    }

    @PostMapping(path = "/trainsLocations")
    @ApiIgnore
    @ApiOperation(value = "Display train location to your Train ", notes = "It will display trains location of given stations into Table Format", response = TrainsBetweenStation.class)
    public List<TrainLocation> getTrainLocation(@RequestBody LocationBody locationBody) {
        return trainService.trainLocation(locationBody.getTrain_no(),locationBody.getDate(),locationBody.getYour_location());
    }
    // *--------------------------------------------*


    // *----- Basic Data Display Functionality -----*
    @GetMapping(path = "/trainFare/{origin}:{destination}")
    @ApiIgnore
    @ApiOperation(value = "Display trains and fare between stations", notes = "It will display all train and fare between given stations into JSON Format", response = TrainsBetweenStation.class)
    public String getTrainFareToTable(@PathVariable String origin, @PathVariable String destination) {
        return trainService.trainFairToTable(origin, destination);
    }

    @GetMapping(path = "/trainFares/{origin}:{destination}")
    @ApiIgnore
    @ApiOperation(value = "get trains and fare between stations", notes = "It will display all train and Fare between given stations into JSON Format", response = TrainsBetweenStation.class)
    public List<TrainsBetweenStation> getTrainFare(@PathVariable String origin, @PathVariable String destination) {
        return trainService.trainFair(origin, destination);
    }
    // *--------------------------------------------*


    // *----- Basic Data Display Functionality -----*
    @PutMapping(path = "/updateAllTrains")
    public String updateAlltrains(@RequestBody List<Train> list) {
        return trainService.updateData(list);
    }

    @GetMapping(path = "/customUpdate")
    @ApiIgnore
    public String customUpdate() {
        return trainService.customUpdation();
    }

    @GetMapping(path = "/displayAllTrains")
    public List<Train> displayAlltrains() {
        return trainService.displayAllTrains();
    }

    @GetMapping(path = "/displaytrains/{trainsNo}")
    @ApiIgnore
    public Train displayTrains(@PathVariable String trainsNo) {
        return trainService.displayTrain(trainsNo);
    }

    @GetMapping(path = "/getTrainByTrainNo/{train_no}")
    public Train getTrainByTrainNo(@PathVariable String train_no) {
        return trainService.getTrainByTrainNo(train_no);
    }


    @GetMapping(path = "/trainExistByTrainNo/{train_no}")
    @ApiIgnore
    public boolean trainExistByTrainNo(@PathVariable String train_no) { return trainService.trainExistByTrainNo(train_no);}

    @GetMapping(path = "/displaytrain/{trainsNo}")
    @ApiIgnore
    public String displayTrain(@PathVariable String trainsNo) {
        return trainService.displayTrainToTable(trainsNo);
    }
    // *---------------------------------------------*

    // *------------------------------------------------- End of Management Functionalities ------------------------------------*

}
