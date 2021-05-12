package railway.reservation.system.Controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import railway.reservation.system.Models.TimeTable;
import railway.reservation.system.Models.Train.Train;
import railway.reservation.system.Models.TrainLocation;
import railway.reservation.system.Models.TrainsBetweenStation;

import railway.reservation.system.Service.TrainService.TrainService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {



    // *-------- Train Service Autowiring -------*
    @Autowired
    TrainService trainService;
    // *-----------------------------------------*




    //  *------- Time Table Functionality -------*
    @GetMapping(path = "/timeTable/{city}")
    @ApiIgnore
    @ApiOperation(value = "get Time Table of Trains",notes = "It Will Display the TimeTable of All Trains From Your Station in JSON format",response = TimeTable.class)
    public List<TimeTable> displayTimeTable(@PathVariable String city) { return trainService.displayTimeTable(city); }

    @GetMapping(path = "/trainTimeTable/{city_name}")
    @ApiOperation(value = "Display Time Table of Trains",notes = "It Will Display the TimeTable of All Trains From Your Station into Table Format ",response = TimeTable.class)
    public String displayTimeToTable(@PathVariable String city_name) { return trainService.displayTimeTableByYourCity(city_name); }
    // *-------------------------------------------*



    // *---Train Between Stations Functionality ---*
    @GetMapping(path = "/trainsBetweenStation/{origin}:{destination}")
    @ApiOperation(value = "Display trains between stations",notes = "It will display all trains between given stations into Table Format",response = TrainsBetweenStation.class)
    public String getTrainsBetweenStationToTable(@PathVariable String origin , @PathVariable String destination){return trainService.trainsBetweenStationToTable(origin,destination);}

    @GetMapping(path = "/trainsBetweenStations/{origin}:{destination}")
    @ApiIgnore
    @ApiOperation(value = "get trains between stations",notes = "It will display all trains     between given stations into JSON Format",response = TrainsBetweenStation.class)
    public List<TrainsBetweenStation> getTrainsBetweenStation(@PathVariable String origin , @PathVariable String destination){return trainService.trainsBetweenStation(origin,destination);}
    // *--------------------------------------------*


    // *------- Train Location Functionality -------*
    @GetMapping(path = "/trainsLocation/{train_info}:{your_location}:{day}")
    @ApiOperation(value = "Display train location to your Train ",notes = "It will display trains location of given stations into JSON Format",response = TrainsBetweenStation.class)
    public String getTrainLocationToTable(@PathVariable String train_info ,@PathVariable String your_location , @PathVariable String day){return trainService.trainLocationToTable(train_info,day,your_location);}

    @GetMapping(path = "/trainsLocations/{train_info}:{your_location}:{day}")
    @ApiIgnore
    @ApiOperation(value = "Display train location to your Train ",notes = "It will display trains location of given stations into Table Format",response = TrainsBetweenStation.class)
    public List<TrainLocation> getTrainLocation(@PathVariable String train_info , @PathVariable String your_location , @PathVariable String day){return trainService.trainLocation(train_info,day,your_location);}
    // *--------------------------------------------*


    // *----- Basic Data Display Functionality -----*
    @GetMapping(path = "/trainFare/{origin}:{destination}")
    @ApiOperation(value = "Display trains and fare between stations",notes = "It will display all train and fare between given stations into JSON Format",response = TrainsBetweenStation.class)
    public String getTrainFareToTable(@PathVariable String origin , @PathVariable String destination){return trainService.trainFairToTable(origin, destination);}

    @GetMapping(path = "/trainFares/{origin}:{destination}")
    @ApiIgnore
    @ApiOperation(value = "get trains and fare between stations",notes = "It will display all train and Fare between given stations into JSON Format",response = TrainsBetweenStation.class)
    public List<TrainsBetweenStation> getTrainFare(@PathVariable String origin , @PathVariable String destination){return trainService.trainFair(origin, destination);}
    // *--------------------------------------------*




    // *----- Basic Data Display Functionality -----*
    @PutMapping(path = "/updateAllTrains")
    @ApiIgnore
    public String updateAlltrains(@RequestBody List<Train> list) {return trainService.updateData(list);}

    @GetMapping(path = "/customUpdate")
    @ApiIgnore
    public String customUpdate(){return trainService.customUpdation();}

    @GetMapping(path = "/displayAllTrains")
    @ApiIgnore
    public List<Train> displayAlltrains() {return trainService.displayAllTrains();}

    @GetMapping(path = "/displaytrains/{trainsNo}")
    @ApiIgnore
    public Train displayTrains(@PathVariable String trainsNo)
    {
        return trainService.displayTrain(trainsNo);
    }

    @GetMapping(path = "/getTrainByTrainNo/{train_no}")
    public Train getTrainByTrainNo(@PathVariable String train_no){return trainService.getTrainByTrainNo(train_no);}

    @GetMapping(path = "/trainExistByTrainNo/{train_no}")
    public boolean trainExistByTrainNo(@PathVariable String train_no){return trainService.trainExistByTrainNo(train_no);}

    @GetMapping(path = "/displaytrain/{trainsNo}")
    @ApiIgnore
    public String displayTrain(@PathVariable String trainsNo)
    {
        return trainService.displayTrainToTable(trainsNo);
    }
    // *---------------------------------------------*

}