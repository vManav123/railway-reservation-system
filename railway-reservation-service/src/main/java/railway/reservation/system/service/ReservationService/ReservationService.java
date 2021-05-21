package railway.reservation.system.service.reservationService;


import railway.reservation.system.model.ticket.ReservedTicket;
import railway.reservation.system.model.ticket.Seat_Id;
import railway.reservation.system.model.ticket.Ticket;
import railway.reservation.system.model.ticket.TicketStatus;

import java.time.LocalDate;

public interface ReservationService {
    public TicketStatus reserveTicket(Ticket ticket);

    public String reserveSeat(String train_no, String class_name, LocalDate date, Long pnr);

    public String reservedTicket(ReservedTicket reservedTicket);

    public ReservedTicket getTicket(long pnr);

    public boolean ticketExistByPNR(long pnr);

    public String ticketCancellation(long pnr);

    public String seatCancellation(String seat_no, String class_name, Seat_Id seat_id);

    public String availableSeats(String start,String destination,LocalDate date);

    public String availableAccommodation(String train_no,LocalDate date);
}
