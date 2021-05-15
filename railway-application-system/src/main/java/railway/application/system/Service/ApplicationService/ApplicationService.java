package railway.application.system.Service.ApplicationService;

import railway.application.system.Models.Forms.ReservationForm;

public interface ApplicationService {

    String userWelcome();

    String getTrain(String train_info);

    String trainTimeTable(String station);

    String trainBetweenStation(String origin, String destination);

    String trainFares(String origin, String destination);

    String trainLocation(String train_info, String your_location, String day);

    String reserveTicket(ReservationForm reservationForm);

    String getPNR(Long pnr);

    String TicketCancellation(long pnr);


}
