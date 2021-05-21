package railway.application.system.service.applicationService;

import org.springframework.http.ResponseEntity;
import railway.application.system.models.forms.AccommodationForm;
import railway.application.system.models.forms.UserForm;
import railway.application.system.models.response.AccommodationBody;
import railway.application.system.models.response.AvailableSeats;
import railway.application.system.models.forms.ReservationForm;
import railway.application.system.models.response.LocationBody;

public interface ApplicationService {

    String userWelcome();

    String getTrain(String train_info);

    String trainTimeTable(String station);

    String trainBetweenStation(String origin, String destination);

    String trainFares(String origin, String destination);

    String trainLocation(LocationBody locationBody);

    String reserveTicket(ReservationForm reservationForm);

    String getPNR(Long pnr);

    String TicketCancellation(long pnr);
    
    ResponseEntity<?> getTrainByTrainNo(String train_no);

    public String availableSeats(AvailableSeats availableSeats);

    public String availableAccommodation(AccommodationBody accommodationBody);

    public String createUserAccount(UserForm userForm);

}
