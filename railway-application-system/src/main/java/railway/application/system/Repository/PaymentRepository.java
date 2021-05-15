package railway.application.system.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import railway.application.system.Models.Payment.Payment;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Long> {
}
