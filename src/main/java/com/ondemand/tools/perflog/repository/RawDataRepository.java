package com.ondemand.tools.perflog.repository;

import com.ondemand.tools.perflog.kafka.models.RawData;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Chandu D - i861116
 * @created 14/11/2022 - 10:20 AM
 * @description
 */
public interface RawDataRepository extends MongoRepository<RawData,String> {

    RawData save(RawData entity);

}
