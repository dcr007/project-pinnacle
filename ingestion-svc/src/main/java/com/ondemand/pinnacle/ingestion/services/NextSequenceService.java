package com.ondemand.pinnacle.ingestion.services;

import com.ondemand.pinnacle.ingestion.models.CustomSequences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author Chandu D - i861116
 * @created 01/12/2022 - 11:44 AM
 * @description -
 * Use a counter collection with a ‘counter name’ as its id, and a ‘seq’ field to store the last used number.
 * collection name id used as the counter name, so it’s easy to guess / remember.
 */
@Service
public class NextSequenceService {
    @Autowired
    private MongoOperations mongo;

    public long getNextSequence(String seqName) {
        CustomSequences counter = mongo.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                CustomSequences.class);
        assert counter != null;
        return counter.getSeq();
    }
}
