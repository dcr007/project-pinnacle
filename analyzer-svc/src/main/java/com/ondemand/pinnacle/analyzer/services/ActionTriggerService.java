package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.analyzer.app.clients.constants.ingestion.IngestionEventStatus;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Chandu D - i861116
 * @created 05/01/2023 - 10:30 AM
 * @description
 */
@Service
@Slf4j
public class ActionTriggerService {
    @Autowired
    PinnacleIngestionQueryService ingestionQueryService;

    @Async("service-td")
    public void triggerQueuedAfterSleep() {
        this.delay(2);
        this.triggerQueued(false);
    }

    public Optional<List<PerfLogModel>> triggerQueued(boolean notify) {
        synchronized (this) {
//            1. collect all Logs in Queued state
            PerfLogModel[] perfLogModels = ingestionQueryService
                    .findByIngestionStatus(IngestionEventStatus.QUEUED);
            List<PerfLogModel> perfLogModelsInQueuedStatus =
                    Stream.of(perfLogModels).collect(Collectors.toList());

//            2. if nothing is collected , nothing to create
            if (perfLogModelsInQueuedStatus.isEmpty()) {
                log.info("No perf-logs in {} state collected from ingestion service. " +
                        "Exiting", IngestionEventStatus.QUEUED);
                return Optional.of(perfLogModelsInQueuedStatus);
            }
//           3. if perflogs collected, then operate on them
            List<String> perfLogIds = perfLogModelsInQueuedStatus.stream().map(PerfLogModel::getPerfLogId)
                    .collect(Collectors.toList());

            log.info("{} Perflogs entities colected with ids: {}", perfLogModelsInQueuedStatus.size(),
                    perfLogIds);

//          TODO:4: update the ingestion status to PROCESSING.
            ingestionQueryService.updateIngestionStatus(IngestionEventStatus.PROCESSING,perfLogIds);

//          TODO 5: filter the perflogs with anomalies and update IngestionStatus to ANOMALIES_DETECTED_TRUE

//          TODO:6: update perflog IngestionStatus to COMPLETE in the ingestion service.
            return Optional.of(perfLogModelsInQueuedStatus);

        }
    }

    private void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            log.error("Error in thread sleep");
            e.printStackTrace();
        }
    }

}
