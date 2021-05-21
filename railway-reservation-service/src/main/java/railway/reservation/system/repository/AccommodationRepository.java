package railway.reservation.system.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.reservation.system.model.ticket.AccommodationDetail;

@Repository
public interface AccommodationRepository extends MongoRepository<AccommodationDetail,String> {
}
