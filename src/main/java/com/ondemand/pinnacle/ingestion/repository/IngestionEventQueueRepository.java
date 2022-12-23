package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.analyzer.models.IngestionEventStatus;
import com.ondemand.pinnacle.ingestion.entities.IngestionEventQueueEntity;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 5:30 PM
 * @description
 */
@Repository
public interface IngestionEventQueueRepository extends JpaRepository<IngestionEventQueueEntity,Long> {

    List<IngestionEventQueueEntity> findByIngestionEventStatus(IngestionEventStatus ingestionEventStatus);

}
