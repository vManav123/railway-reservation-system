package railway.management.system.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.management.system.Models.Train;

@Repository
public interface TrainRepository extends MongoRepository<Train,Long> {

}
