package railway.reservation.system.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import railway.reservation.system.Models.Ticket.ReserveSeats;
import railway.reservation.system.Models.Ticket.Seat_Id;

public interface ReservedSeatsRepository extends MongoRepository<ReserveSeats, Seat_Id> {
}
