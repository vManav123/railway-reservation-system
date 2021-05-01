package railway.management.system.Service;


import railway.management.system.Models.TimeTable;
import railway.management.system.Models.Train;
import railway.management.system.Models.TrainLocation;
import railway.management.system.Models.TrainsBetweenStation;

import java.util.List;

public interface TrainService {

    public List<Train> displayAllTrains();

    public Train displayTrain(Long trainNo);

    public String updateData();

    // *-------------- Time Table Functionality ---------------*

    public List<TimeTable> displayTimeTable();
    public List<TimeTable> displayTimeTableByYourCity(String city);

    // *--------------------------------------------------------*


    // *----------------- Train Between Station Functionality --------------------*

    public List<TrainsBetweenStation> trains_between_station(String origin ,  String destination);

    // *--------------------------------------------------------------------------*


    // Train Location Functionality
    public String trainLocation(String train_search,String day,String your_location);

}
