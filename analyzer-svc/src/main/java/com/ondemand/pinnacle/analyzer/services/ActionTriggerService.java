package com.ondemand.pinnacle.analyzer.services;

import com.ondemand.pinnacle.analyzer.app.clients.constants.ingestion.IngestionEventStatus;
import com.ondemand.pinnacle.analyzer.models.StackClassification;
import com.ondemand.pinnacle.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.analyzer.models.ingestion.PerfLogModel;
import com.ondemand.pinnacle.analyzer.repository.StackClassificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
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

//    @Autowired
    PerfLogAnalyzerServiceImpl perfLogAnalyzerService ;

    @Autowired
    StackClassificationRepository stackClassificationRepository;

    @Autowired
    private KieSession session;

    @Async("service-td")
    public void triggerQueuedAfterSleep() {
        this.delay(2);
        this.triggerQueued(false);
    }
    public Optional<List<Map<StackCategory, ArrayList<StackClassification>>>> triggerAnalyze(boolean notify){

        PerfLogModel[] perfLogModels = ingestionQueryService
                .findByIngestionStatus(IngestionEventStatus.ANALYZING);
        List<PerfLogModel> perfLogModelsInQueuedStatus =
                Stream.of(perfLogModels).collect(Collectors.toList());

        List<Map<StackCategory, ArrayList<StackClassification>>> analyzedList = null;

        analyzedList = perfLogModelsInQueuedStatus.stream().map(this::analyze).collect(Collectors.toList());
        return Optional.of(analyzedList);
    }

    public  Optional<List<StackClassification>> triggerAnomalyDetection(){

        Optional<List<Map<StackCategory, ArrayList<StackClassification>>>> classifiedMetrics = null;

        List<StackClassification> stackClassification = new ArrayList<>();

//        TODO: for each record in analyzer_svc_stack_classification table
//         with NOT is_metric_validated() check if every given metric exceeds threshold set
//         and update  has_threshold_exceeded
//         then return classifiedMetrics

        PerfLogModel[] perfLogModels = ingestionQueryService
                .findByIngestionStatus(IngestionEventStatus.ANALYZING_COMPLETE);
        List<String> perfLogIdsToBeValidated = Arrays.stream(perfLogModels).map(PerfLogModel::getPerfLogId)
                .collect(Collectors.toList());
        

        perfLogIdsToBeValidated.forEach(id ->{
            List<StackClassification> listOfStackClassifications =
                    stackClassificationRepository.findByPerfLogId(id);
            stackClassification.addAll(listOfStackClassifications);
            listOfStackClassifications.forEach(stack ->{
                session.insert(stack);
                session.fireAllRules();
            });
        });
        stackClassificationRepository.saveAll(stackClassification);
        return stackClassification.size()>0?
                Optional.of(stackClassification):Optional.empty();

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
//            ingestionQueryService.updateIngestionStatus(IngestionEventStatus.ANALYZING,perfLogIds);
            perfLogModelsInQueuedStatus.forEach(this::process);

//            perfLogAnalyzerService.getCallStackAnalysis()
            // analyze PerfLog



//          TODO: check for anomolies

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

    private void process(PerfLogModel perfLogModel) {
        synchronized (this) {
            try{
                ingestionQueryService.updateIngestionStatus(IngestionEventStatus.ANALYZING,perfLogModel.getPerfLogId());
                log.info("analysis for perfLogId: {}\n", perfLogModel.getPerfLogId());
//                log.info(perfLogAnalyzerService.getCallStackAnalysis(perfLogModel).toString());
            }catch (RuntimeException e){
                log.error("Error while executing callStackAnalysis: {}",e.getMessage());
            }
        }
    }

    private Map<StackCategory, ArrayList<StackClassification>>  analyze(PerfLogModel perfLogModel) {
        synchronized (this) {
            try{
//                ingestionQueryService.updateIngestionStatus(IngestionEventStatus.ANALYZING,perfLogModel.getPerfLogId());
                log.info("analysis for perfLogId: {}\n", perfLogModel.getPerfLogId());
                perfLogAnalyzerService = new PerfLogAnalyzerServiceImpl(perfLogModel);

                Map<StackCategory, ArrayList<StackClassification>> callStackAnalysis =
                        perfLogAnalyzerService.getCallStackAnalysis(perfLogModel);

                for(var entry: callStackAnalysis.entrySet()){
                    log.info("saving stackCategory key: {}",entry.getKey());

                    stackClassificationRepository.saveAll(entry.getValue());
                  /*  entry.getValue().forEach(
                            stackClassification -> {
                                log.info("saving PerfLog ID: {} and qualifierName: {}",
                                        stackClassification.getPerfLogId(),
                                        stackClassification.getQualifierName());
                                        stackClassificationRepository.save(stackClassification);
                            }
                    );*/

                }

                ingestionQueryService.updateIngestionStatus(IngestionEventStatus.ANALYZING_COMPLETE,perfLogModel.getPerfLogId());
                log.info("Updated event status to {} for perfLogId {} ", IngestionEventStatus.ANALYZING_COMPLETE,perfLogModel.getPerfLogId());
                return callStackAnalysis;
            }catch (RuntimeException e){
                log.error("Error while executing callStackAnalysis: {}",e.getMessage());
            }
        }
        return null;
    }
}