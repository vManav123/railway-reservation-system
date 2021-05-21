package railway.reservation.system.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.reservation.system.model.ticket.Trains_Seats;

@Repository
public interface SeatRepository extends MongoRepository<Trains_Seats, String> {
}
