package railway.reservation.system.Service.ReservationService;


import railway.reservation.system.Models.Ticket.ReservedTicket;
import railway.reservation.system.Models.Ticket.Seat_Id;
import railway.reservation.system.Models.Ticket.Ticket;
import railway.reservation.system.Models.Ticket.TicketStatus;

import java.time.LocalDate;

public interface ReservationService {
    public TicketStatus reserveTicket(Ticket ticket);

    public String reserveSeat(String train_no, String class_name, LocalDate date, Long pnr);

    public String reservedTicket(ReservedTicket reservedTicket);

    public ReservedTicket getTicket(long pnr);

    public boolean ticketExistByPNR(long pnr);

    public String ticketCancellation(long pnr);

    public String seatCancellation(String seat_no, String class_name, Seat_Id seat_id);
}
