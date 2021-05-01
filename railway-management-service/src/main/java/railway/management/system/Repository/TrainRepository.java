package railway.management.system.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.management.system.Models.Train;

@Repository // It is a class-level annotation. The repository is a DAOs (Data Access Object) that access the database directly. The repository does all the operations related to the database.
public interface TrainRepository extends MongoRepository<Train,Long> {

}
