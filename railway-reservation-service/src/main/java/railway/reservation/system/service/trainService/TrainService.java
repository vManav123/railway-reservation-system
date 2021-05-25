package railway.reservation.system.service.trainService;


import railway.reservation.system.model.TimeTable;
import railway.reservation.system.model.train.Train;
import railway.reservation.system.model.TrainLocation;
import railway.reservation.system.model.TrainsBetweenStation;

import java.time.LocalDate;
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
    public List<TrainsBetweenStation> trainsBetweenStation(String origin, String destination);

    public String trainsBetweenStationToTable(String origin, String destination);
    // *--------------------------------------------------------------------------*


    // *----------------------- train Fair Functionality -------------------------*
    public List<TrainLocation> trainLocation(String train_search, LocalDate date, String your_location);

    public String trainLocationToTable(String train_search, LocalDate date, String your_location);
    // *---------------------------------------------------------------------------*


    // *-------------------------- train Fair Functionality ------------------------*
    public String trainFairToTable(String origin, String destination);

    public List<TrainsBetweenStation> trainFair(String origin, String destination);

    public String deleteAllTrains(String confirmation);

    public String deleteTrain(String train_no,String confirmation);

    public String addTrain(Train train);
    // *-----------------------------------------------------------------------------*

}
