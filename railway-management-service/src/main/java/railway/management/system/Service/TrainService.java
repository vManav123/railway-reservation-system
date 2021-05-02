package railway.management.system.Service;


import railway.management.system.Models.TimeTable;
import railway.management.system.Models.Train;
import railway.management.system.Models.TrainLocation;
import railway.management.system.Models.TrainsBetweenStation;

import java.util.List;

public interface TrainService {
    // *----- Basic Data Display Functionality -----*

    public List<Train> displayAllTrains();
    public Train displayTrain(Long trainNo);
    public String updateData(List<Train> trains);
    public String customUpdation();

    // *---------------------------------------------*

    // *-------------- Time Table Functionality ---------------*

    public String displayTimeTable();
    public String displayTimeTableByYourCity(String city);

    // *--------------------------------------------------------*


    // *----------------- Train Between Station Functionality --------------------*

    public List<TrainsBetweenStation> trainsBetweenStation(String origin ,  String destination);
    public String trainsBetweenStationToTable(String origin , String destination);

    // *--------------------------------------------------------------------------bv *


    // *------------------------------- train Fair Functionality ----------------------------------*

    public String trainLocation(String train_search,String day,String your_location);

    // *-------------------------------------------------------------------------------------------*


    // *------------------------------- train Fair Functionality ----------------------------------*

    public String trainFairToTable(String origin ,  String destination);
    public List<TrainsBetweenStation> trainFair(String origin ,  String destination);
    // *-------------------------------------------------------------------------------------------*

}
