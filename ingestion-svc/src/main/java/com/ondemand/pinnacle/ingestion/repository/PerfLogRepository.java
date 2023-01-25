package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.ingestion.models.PerfLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 15/11/2022 - 4:14 PM
 * @description
 */
@Repository
public interface PerfLogRepository extends
        MongoRepository<PerfLog,String> {
    List<PerfLog> findByPerfLogIdIn(List<String> perfLogId);
}
