package railway.reservation.system.Service.TrainService;


import railway.reservation.system.Models.TimeTable;
import railway.reservation.system.Models.Train.Train;
import railway.reservation.system.Models.TrainLocation;
import railway.reservation.system.Models.TrainsBetweenStation;

import java.util.List;

public interface TrainService {
    // *------------------- Basic Data Display Functionality -------------------*
    public List<Train> displayAllTrains();
    public String displayTrainToTable(String trainNo);
    public Train displayTrain(String trainNo);
    public String updateData(List<Train> trains);
    public String customUpdation();
    public Train getTrainByTrainNo(String train_no);
    public boolean trainExistByTrainNo(String train_no);
    // *------------------------------------------------------------------------*

    // *----------------------- Time Table Functionality -----------------------*
    public List<TimeTable> displayTimeTable(String city);
    public String displayTimeTableByYourCity(String city);
    // *-------------------------------------------------------------------------*


    // *---------------- Train Between Station Functionality --------------------*
    public List<TrainsBetweenStation> trainsBetweenStation(String origin ,  String destination);
    public String trainsBetweenStationToTable(String origin , String destination);
    // *--------------------------------------------------------------------------*


    // *----------------------- train Fair Functionality -------------------------*
    public List<TrainLocation> trainLocation(String train_search,String day,String your_location);
    public String trainLocationToTable(String train_search,String day,String your_location);
    // *---------------------------------------------------------------------------*


    // *-------------------------- train Fair Functionality ------------------------*
    public String trainFairToTable(String origin ,  String destination);
    public List<TrainsBetweenStation> trainFair(String origin ,  String destination);
    // *-----------------------------------------------------------------------------*

}
