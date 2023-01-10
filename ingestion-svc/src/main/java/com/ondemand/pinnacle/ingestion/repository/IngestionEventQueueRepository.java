package com.ondemand.pinnacle.ingestion.repository;

import com.ondemand.pinnacle.ingestion.analyzer.models.IngestionEventStatus;
import com.ondemand.pinnacle.ingestion.entities.IngestionEventQueueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chandu D - i861116
 * @created 21/12/2022 - 5:30 PM
 * @description
 */
@Repository
public interface IngestionEventQueueRepository extends JpaRepository<IngestionEventQueueEntity,Long> {

    List<IngestionEventQueueEntity> findByIngestionEventStatus(IngestionEventStatus ingestionEventStatus);

    @Transactional
    @Modifying
    @Query("update IngestionEventQueueEntity IEQ set IEQ.ingestionEventStatus = :status where IEQ.perfLogId = :perfLogId")
    void updateIngestionEventStatus(@Param(value = "perfLogId") String perfLogId, @Param(value = "status") IngestionEventStatus status);

}
