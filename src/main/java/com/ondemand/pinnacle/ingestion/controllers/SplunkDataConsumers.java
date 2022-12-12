package com.ondemand.pinnacle.ingestion.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.pinnacle.ingestion.models.CallStack;
import com.ondemand.pinnacle.analyzer.models.SegregatedStack;
import com.ondemand.pinnacle.ingestion.models.SplunkPayLoad;
import com.ondemand.pinnacle.analyzer.models.enums.CallCategory;
import com.ondemand.pinnacle.ingestion.kafka.producer.Producer;
import com.ondemand.pinnacle.ingestion.services.CallStackService;
import com.ondemand.pinnacle.analyzer.services.FetchPerfLogDataService;
import com.ondemand.pinnacle.analyzer.services.PerfLogAnalyzerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
    public ResponseEntity<Map<CallCategory, ArrayList<SegregatedStack>>>
    parseCallStack(@RequestBody CallStack callStack) throws JsonProcessingException {

        log.info("Received call stack :\n{}", callStack);
        Map<CallCategory, ArrayList<SegregatedStack>> callCategoryMap = new HashMap<>();
        return ResponseEntity.ok(analyzePerfLogService.analyzeCallStack(callStack, callCategoryMap));
    }

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


}
