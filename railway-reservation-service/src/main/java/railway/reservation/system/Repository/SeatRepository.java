package railway.reservation.system.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.reservation.system.Models.Train.Trains_Seats;

@Repository
public interface SeatRepository extends MongoRepository<Trains_Seats,Long> {
}
