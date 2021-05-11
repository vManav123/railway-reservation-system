package railway.application.system.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import railway.application.system.Models.Forms.ReservationForm;
import railway.application.system.Service.ApplicationService.ApplicationService;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api( tags = "Application")
@RequestMapping(path = "/application")
public class ApplicationController {


    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = "/swagger")
    @ApiIgnore
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("localhost:8084/swagger-ui/");
    }


    // *-------------- Public Functionalities -------------*
    @GetMapping(path = "/trainDetail/{train_info}")
    @ApiOperation(value = "Display details of trains",notes = "It will display the details of then trains")
    public String getTrainDetail(@PathVariable String train_info){return applicationService.getTrain(train_info);}

    @GetMapping(path = "/trainTimeTable/{station}")
    @ApiOperation(value = "Display Time Table of Trains",notes = "It will display the timeTable of all trains from your station")
    public String trainTimeTable(@PathVariable String station){return applicationService.trainTimeTable(station);}

    @GetMapping(path = "/trainBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations",notes = "It will display all trains between given stations")
    public String trainBetweenStation(@PathVariable String origin , @PathVariable String destination){return applicationService.trainBetweenStation(origin, destination);}

    @GetMapping(path = "/trainFares/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations",notes = "It will display all trains and fare between given stations")
    public String trainFares(@PathVariable String origin , @PathVariable String destination){return applicationService.trainFares(origin, destination);}

    @GetMapping(path = "/trainLocation/{train_info}:{your_location}:{day}")
    @ApiOperation(value = "Display train location to your Train ",notes = "It will display trains location of given stations")
    public String trainLocation(@PathVariable String train_info , @PathVariable String your_location, @PathVariable String day){return applicationService.trainLocation(train_info, your_location, day);}
    // *---------------------------------------------------*



    // *------- Ticket Reservation Functionalities -------*
    @PostMapping(path = "/reserveTicket")
    public String reservedTicket(@RequestBody ReservationForm reservationForm){return applicationService.reserveTicket(reservationForm);}
    // *--------------------------------------------------*

}
