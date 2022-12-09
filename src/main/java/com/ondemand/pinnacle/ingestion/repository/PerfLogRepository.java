package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.ingestion.models.PerfLog;
import com.ondemand.pinnacle.ingestion.models.SplunkPayLoad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Chandu D - i861116
 * @created 15/11/2022 - 4:14 PM
 * @description
 */
@Repository
public interface PerfLogRepository extends
        MongoRepository<PerfLog,String> {
}
