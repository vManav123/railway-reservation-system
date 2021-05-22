package railway.reservation.system.service.seatObservationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.repository.ReservedSeatsRepository;
import railway.reservation.system.repository.ReservedTicketRepository;
import railway.reservation.system.repository.SeatRepository;
import railway.reservation.system.repository.TrainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatObservationServiceImpl implements SeatObservationService{


    // *---------------- Autowiring Reference Variables -----------------*
    @Autowired
    private ReservedTicketRepository reservedTicketRepository;
    @Autowired
    private ReservedSeatsRepository reservedSeatsRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private SeatRepository seatRepository;
    // *-----------------------------------------------------------------*


    // *------------------------ Functionality --------------------------*


    public List<ReservedTicket> checkWaitingTickets()
    {
        List<ReservedTicket> reservedTickets = reservedTicketRepository.findAll().stream().filter(p->p.getStatus().equalsIgnoreCase("Waiting")).collect(Collectors.toList());
        if(reservedTickets.isEmpty())
            return new ArrayList<>();
        else
            return reservedTickets;
    }


    // *-----------------------------------------------------------------*




}
