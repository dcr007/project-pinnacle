package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.ingestion.entities.IngestionEventQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 5:30 PM
 * @description
 */
public interface IngestionEventQueueRepository extends JpaRepository<IngestionEventQueueEntity,Long> {
}
