package com.ondemand.tools.perflog.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.ondemand.tools.perflog.kafka.models.CallStack;
import com.ondemand.tools.perflog.kafka.models.SegregatedStack;
import com.ondemand.tools.perflog.kafka.models.SplunkPayLoad;
import com.ondemand.tools.perflog.kafka.models.enums.CallCategory;
import com.ondemand.tools.perflog.kafka.producer.Producer;
import com.ondemand.tools.perflog.services.CallStackService;
import com.ondemand.tools.perflog.services.FetchPerfLogDataService;
import com.ondemand.tools.perflog.services.SegregateStackService;
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
 * @description - Swagger UI @ - http://localhost:8851/svc/perflog/swagger-ui/
 */
@RestController
@RequestMapping(value = "api/internal/parser")
@Slf4j
@AllArgsConstructor
public class ParserControllers {

    @Autowired
    private final Producer producer;
    @Autowired
    KafkaTemplate<String, CallStack> kafkaTemplate;
    @Autowired
    private FetchPerfLogDataService fetchPerfLogDataService;
//    String TOPIC_NAME = "perflog-for-dwr-calls";
    @Autowired
    private SegregateStackService segregateStackService;
    @Autowired
    private CallStackService callStackService;

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
        return ResponseEntity.ok(segregateStackService.segregatedStack(callStack, callCategoryMap));
    }

    @PostMapping("/saveCallStack")
    public String saveCallStack(@RequestBody CallStack callStack) {
        return callStackService.save(callStack);
    }

    @PostMapping("/sendToCallStackQueue")
    public String sendCallStackToQueue(@RequestBody CallStack callStack) {
        producer.sendMessage(callStack);
        return "Message published sucessfully";
    }

    @PostMapping("/sendToDwrQueue")
    public HttpStatus sendCallStackToQueue(@RequestBody SplunkPayLoad dwrStack)
            throws ExecutionException, InterruptedException, TimeoutException {

        return producer.sendDwrLogs(dwrStack).getStatusCode();
        // return "Message published sucessfully";
    }


    @PostMapping("/postRawCallStack")
    public HttpStatus postRawCallStack(@RequestBody JsonNode callStack) {

        HttpStatus status =
                callStackService.save(callStack);
        return status;
    }

    @PostMapping(value = "/postSplunkPayLoad")
    public HttpStatus postSplunkPayLoad(@RequestBody SplunkPayLoad splunkPayLoad) {

        HttpStatus status =
                callStackService.save(splunkPayLoad);
        return status;
    }
}
