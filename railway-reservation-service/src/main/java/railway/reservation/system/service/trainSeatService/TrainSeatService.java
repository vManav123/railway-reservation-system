package railway.reservation.system.service.trainSeatService;

import railway.reservation.system.model.ticket.Trains_Seats;

public interface TrainSeatService {
    public String addData();

    public Trains_Seats getTrainSeats(String train_no);
}
