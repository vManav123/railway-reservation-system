package railway.management.system.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import railway.management.system.Models.TimeTable;
import railway.management.system.Models.Train;
import railway.management.system.Service.TrainService;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    TrainService trainService;

    @GetMapping(path = "/timeTable")
    public List<TimeTable> displayTimeTable() { return trainService.displayTimeTable(); }

    @GetMapping(path = "/displayAllTrains")
    public List<Train> displayAlltrains() {return trainService.displayAllTrains();}

    @GetMapping(path = "/displaytrain/{trainsNo}")
    public Train displayTrain(@PathVariable Long trainsNo)
    {
        return trainService.displayTrain(trainsNo);
    }

}
