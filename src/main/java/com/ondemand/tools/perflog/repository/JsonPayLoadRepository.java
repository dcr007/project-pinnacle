package com.ondemand.tools.perflog.repository;

import com.mongodb.client.MongoDatabase;
import com.ondemand.tools.perflog.models.JsonPayLoad;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Chandu D - i861116
 * @created 14/11/2022 - 11:21 AM
 * @description
 */
public interface JsonPayLoadRepository extends MongoRepository<JsonPayLoad,String> {
    JsonPayLoad save(JsonPayLoad rawEntity);
}
