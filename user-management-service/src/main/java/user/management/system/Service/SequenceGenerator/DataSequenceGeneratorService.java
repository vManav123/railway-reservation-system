package user.management.system.Service.SequenceGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import user.management.system.Models.Bank.BankSequence;
import user.management.system.Models.User.UserSequence;

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
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }

    public Long getBankSequenceNmber(String sequenceName) {
        Query query = new Query(Criteria.where("id").is(sequenceName));
        Update update = new Update().inc("seq", 1);
        BankSequence counter = mongoOperations.findAndModify(query, update, options().returnNew(true).upsert(true), BankSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
