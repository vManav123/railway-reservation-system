package bank.mangement.service.service.sequenceGenerator;

import bank.mangement.service.model.bank.BankSequence;
import bank.mangement.service.model.bank.TransactionalSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
public class DataSequenceGeneratorService {
    @Autowired
    private MongoOperations mongoOperations;


    public Long getBankSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        BankSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), BankSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public Long getTransactionSequenceNumber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        TransactionalSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), TransactionalSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 966660909;
    }
}
