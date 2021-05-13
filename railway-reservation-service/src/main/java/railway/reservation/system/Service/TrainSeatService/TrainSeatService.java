package railway.reservation.system.Service.TrainSeatService;

import railway.reservation.system.Models.Ticket.Trains_Seats;

public interface TrainSeatService {
    public String addData();

    public Trains_Seats getTrainSeats(String train_no);
}
