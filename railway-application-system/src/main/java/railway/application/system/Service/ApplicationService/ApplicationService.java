package railway.application.system.Service.ApplicationService;

import railway.application.system.Models.Forms.ReservationForm;

public interface ApplicationService {
    public String getTrain(String train_info);

    public String trainTimeTable(String station);

    public String trainBetweenStation(String origin,String destination);

    public String trainFares(String origin,String destination);

    public String trainLocation(String train_info,String your_location,String day);

    public String reserveTicket(ReservationForm reservationForm);
}
