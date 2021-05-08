package railway.reservation.system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import railway.reservation.system.Service.TrainSeatService.TrainSeatService;


@RestController
@RequestMapping(path = "/reservation")
public class ReservationController {

    @Autowired
    private TrainSeatService trainSeatService;

    // *--------------------------- Train Seat Service Functionality -------------------------*
    @GetMapping(path = "/addData")
    public String addData(){return trainSeatService.addData();}
    @GetMapping(path = "/welcome")
    public String welcome(){return "Welcome to Pakistan Railway Reservation portal";}
    // *--------------------------------------------------------------------------------------*

}
