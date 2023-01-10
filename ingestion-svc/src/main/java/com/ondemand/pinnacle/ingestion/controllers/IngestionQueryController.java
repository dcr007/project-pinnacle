package com.ondemand.pinnacle.ingestion.controllers;

import com.ondemand.pinnacle.ingestion.analyzer.models.IngestionEventStatus;
import com.ondemand.pinnacle.ingestion.entities.IngestionEventQueueEntity;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import com.ondemand.pinnacle.ingestion.repository.IngestionEventQueueRepository;
import com.ondemand.pinnacle.ingestion.repository.PerfLogRepository;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 12:28 PM
 * @description - Swagger UI @ - <a href="http://localhost:8851/svc/ingestion/swagger-ui/">...</a>
 */
@RestController
@RequestMapping(value = "${app-security.endpoint.secure}/internal",
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE
 )
@Api(tags = "Internal Queries",value = "Query controller for ingestion svc.")
@Slf4j
@AllArgsConstructor
public class IngestionQueryController {

    @Autowired
    IngestionEventQueueRepository ingestionEventQueueRepository;

    @Autowired
    PerfLogRepository perfLogRepository;

      /**
     * @description  - 1. get all PerflogId's from ingestion-event-queue
     *                 2. for all collected perflogId's get their corresponding perflogs from NoSQL.
     * @param status
     * @return
     */
    @GetMapping("/query/findByIngestionSvcStatus/{status}")
    public ResponseEntity<List<PerfLog>> findByEventLogStatus(@PathVariable IngestionEventStatus status){
        log.info("Executing controller call to `findByIngestionSvcStatus` for status {}",status);
        List<IngestionEventQueueEntity> logsInQueuedStatus = ingestionEventQueueRepository
                                                            .findByIngestionEventStatus(status);
        List<String> perfLogIds = logsInQueuedStatus.stream()
                .map(IngestionEventQueueEntity::getPerfLogId).collect(Collectors.toList()) ;
        log.info("# perfLogIds fetched {}",perfLogIds.size());
        log.info("perfLogIds fetched {}",perfLogIds);

        List<PerfLog> perfLogsToBeProcessed = perfLogRepository.findByPerfLogIdIn(perfLogIds);


        return ResponseEntity.ok(perfLogsToBeProcessed);
    }

    @PostMapping("/command/updateIngestionStatus/{status}")
    public ResponseEntity<?> updateIngestionEventStatus(@PathVariable  IngestionEventStatus status,
                                                        @RequestBody @NotNull List<String> perfLogIds){
        log.info("Executing controller call to `updateIngestionEventStatus` to status {}",status);

        perfLogIds.stream().forEach(perfLogId -> ingestionEventQueueRepository.updateIngestionEventStatus(perfLogId,status.name()));

        log.info("Status updated successfully");
        return ResponseEntity.ok().build();


    }


}
