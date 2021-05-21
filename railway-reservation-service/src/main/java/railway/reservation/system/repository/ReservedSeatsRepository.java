package railway.reservation.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import railway.reservation.system.model.ticket.ReserveSeats;
import railway.reservation.system.model.ticket.Seat_Id;

public interface ReservedSeatsRepository extends MongoRepository<ReserveSeats, Seat_Id> {
}
