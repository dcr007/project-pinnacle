package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.ingestion.models.CallStack;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Chandu D - i861116
 * @created 01/11/2022 - 8:54 PM
 * @description
 */
@Repository
public interface CallStackRepository extends MongoRepository<CallStack,String> {
    Optional<CallStack> findById(String id);
}
