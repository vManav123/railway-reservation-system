package railway.reservation.system.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.reservation.system.Models.Ticket.ReservedTicket;

@Repository
public interface ReservedTicketRepository extends MongoRepository<ReservedTicket,Long> {
}