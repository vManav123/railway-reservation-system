package railway.reservation.system.service.sequenceGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import railway.reservation.system.model.ticket.TicketSequence;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class DataSequenceGeneratorService {
    @Autowired
    private MongoOperations mongoOperations;

    public Long getUserSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        TicketSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), TicketSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
