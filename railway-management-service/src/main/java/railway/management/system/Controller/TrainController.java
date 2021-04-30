package railway.management.system.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import railway.management.system.Models.TimeTable;
import railway.management.system.Models.Train;
import railway.management.system.Service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {



    //  *----- Train Service Autowiring -----*
    @Autowired
    TrainService trainService;



    //  *----- Time Table Functionality -----*
    @GetMapping(path = "/timeTable")
    public List<TimeTable> displayTimeTable() { return trainService.displayTimeTable(); }

    @GetMapping(path = "/timeTableByYourCity/{city_name}")
    public List<TimeTable> displayTimeTable(@PathVariable String city_name) { System.out.println(city_name); return trainService.displayTimeTableByYourCity(city_name); }



    //  *----- Basic Data Display Functionality -----*
    @GetMapping(path = "/displayAllTrains")
    public List<Train> displayAlltrains() {return trainService.displayAllTrains();}

    @GetMapping(path = "/displaytrain/{trainsNo}")
    public Train displayTrain(@PathVariable Long trainsNo)
    {
        return trainService.displayTrain(trainsNo);
    }
    //  *---------------------------------------------*



}
