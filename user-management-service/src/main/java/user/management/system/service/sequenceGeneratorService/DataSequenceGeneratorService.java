package user.management.system.service.sequenceGeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import user.management.system.model.bank.TransactionalSequence;
import user.management.system.model.user.UserSequence;


import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class DataSequenceGeneratorService {
    @Autowired
    private MongoOperations mongoOperations;

    public Long getUserSequenceNmber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        UserSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), UserSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 10001;
    }

    public Long getTransactionSequenceNmber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        TransactionalSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), TransactionalSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 966660909;
    }
}
