package com.ondemand.pinnacle.ingestion.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.pinnacle.ingestion.analyzer.models.IngestionEventStatus;
import com.ondemand.pinnacle.ingestion.analyzer.models.enums.StackCategory;
import com.ondemand.pinnacle.ingestion.analyzer.services.FetchPerfLogDataService;
import com.ondemand.pinnacle.ingestion.analyzer.services.PerfLogAnalyzerService;
import com.ondemand.pinnacle.ingestion.entities.IngestionEventQueueEntity;
import com.ondemand.pinnacle.ingestion.repository.PerfLogRepository;
import com.ondemand.pinnacle.ingestion.services.CallStackService;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.ingestion.analyzer.models.StackClassification;
import com.ondemand.pinnacle.ingestion.models.PerfLog;
import com.ondemand.pinnacle.ingestion.models.SplunkPayLoad;
import com.ondemand.pinnacle.ingestion.kafka.producer.Producer;
import com.ondemand.pinnacle.ingestion.repository.IngestionEventQueueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * @author Chandu D - i861116
 * @created 12/10/2022 - 12:28 PM
 * @description - Swagger UI @ - <a href="http://localhost:8851/svc/ingestion/swagger-ui/">...</a>
 */
@RestController
@RequestMapping(value = "api/external/webhook")
@Slf4j
@AllArgsConstructor
public class SplunkDataConsumers {

    @Autowired
    private final Producer producer;
    @Autowired
    KafkaTemplate<String, CallStack> kafkaTemplate;
    @Autowired
    private FetchPerfLogDataService fetchPerfLogDataService;
//    String TOPIC_NAME = "perflog-for-dwr-calls";
    @Autowired
    private PerfLogAnalyzerService analyzePerfLogService;
    @Autowired
    private CallStackService callStackService;

    @Autowired
    IngestionEventQueueRepository ingestionEventQueueRepository;

    @Autowired
    PerfLogRepository perfLogRepository;

    @PostMapping("/sendToDwrQueue")
    public HttpStatus sendSplunkPayLoadToQueue(@RequestBody SplunkPayLoad splunkPayLoad)
            throws ExecutionException, InterruptedException, TimeoutException {
        return producer.sendDwrLogs(splunkPayLoad).getStatusCode();

    }
    @PostMapping(value = "/postSplunkPayLoad")
    public HttpStatus postSplunkPayLoad(@RequestBody SplunkPayLoad splunkPayLoad) {

        HttpStatus status =
                callStackService.save(splunkPayLoad);
        return status;
    }

    @GetMapping("/getCallStack")
    public ResponseEntity<CallStack> getCallStack() throws JsonProcessingException {
        return ResponseEntity.ok(fetchPerfLogDataService.fetchData());
    }

    @GetMapping("/getCallStackById")
    public CallStack getCallStackById(@RequestParam("id") String id) throws JsonProcessingException {
        return callStackService.getCallStackById(id);
    }

    @PostMapping("/parseCallStack")
    public ResponseEntity<Map<StackCategory, ArrayList<StackClassification>>>
    parseCallStack(@RequestBody CallStack callStack) throws JsonProcessingException {

        log.info("Received call stack :\n{}", callStack);
        Map<StackCategory, ArrayList<StackClassification>> callCategoryMap = new HashMap<>();
        return ResponseEntity.ok(analyzePerfLogService.analyzeCallStack(callStack, callCategoryMap));
    }

    /*
    @PostMapping("/getAnalysisResult")
    public ResponseEntity<AnalysisResult>
    parseCallStack(@RequestBody PerfLog perfLog) throws JsonProcessingException {

        log.info("Received perfLog stack :\n{}", perfLog);
        Map<StackCategory, ArrayList<StackClassification>> callCategoryMap = new HashMap<>();
        return ResponseEntity.ok(analyzePerfLogService.generateReport(perfLog));
    }*/

    @PostMapping("/saveCallStack")
    public String saveCallStack(@RequestBody CallStack callStack) {

        return callStackService.save(callStack);
    }

    @PostMapping("/sendToCallStackQueue")
    public String sendSplunkPayLoadToQueue(@RequestBody CallStack callStack) {
        producer.sendMessage(callStack);
        return "Message published sucessfully";
    }



    @PostMapping("/postRawCallStack")
    public HttpStatus postRawCallStack(@RequestBody JsonNode callStack) {

        HttpStatus status =
                callStackService.save(callStack);
        return status;
    }

    /**
     * @description  - 1. get all PerflogId's from ingestion-event-queue
     *                 2. for all collected perflogId's get their corresponding perflogs from NoSQL.
     * @param status
     * @return
     */
    @GetMapping("/findByIngestionSvcStatus/{status}")
    public ResponseEntity<List<PerfLog>> findByEventLogStatus(@PathVariable IngestionEventStatus status){

        List<IngestionEventQueueEntity> logsInQueuedStatus = ingestionEventQueueRepository
                                                            .findByIngestionEventStatus(status);
        List<String> perfLogIds = logsInQueuedStatus.stream()
                .map(IngestionEventQueueEntity::getPerfLogId).collect(Collectors.toList()) ;
        log.info("# perfLogIds fetched {}",perfLogIds.size());
        log.info("perfLogIds fetched {}",perfLogIds);
        List<PerfLog> perfLogsToBeProcessed = perfLogRepository.findByPerfLogIdIn(perfLogIds);
        return ResponseEntity.ok(perfLogsToBeProcessed);
    }

}
