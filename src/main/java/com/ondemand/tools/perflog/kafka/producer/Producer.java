package com.ondemand.tools.perflog.kafka.producer;

/*
  @author Chandu D - i861116
 * @created 07/11/2022 - 10:39 AM
 * @description
 */


import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.models.PerfLog;
import com.ondemand.tools.perflog.models.SplunkPayLoad;
import com.ondemand.tools.perflog.models.SplunkResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.core.convert.ConversionService;
@Component
@Slf4j
@AllArgsConstructor
public final class Producer {

    @Autowired
    private final KafkaTemplate<String, CallStack> kafkaTemplate;
//    @Autowired
//    ConversionService conversionService;

    @Autowired
    private final KafkaTemplate<String, SplunkPayLoad> splunkPayLoadKafkaTemplate;

    public void sendMessage(CallStack callStack) {
        String topicName = "topic-callStack-call";
        String messageId = topicName+"-"+(Instant.now().toEpochMilli() + "-").concat(UUID.randomUUID().toString());

//        callStack.setId(messageId);
        ListenableFuture<SendResult<String, CallStack>> future = kafkaTemplate.send(topicName,messageId,callStack);



        //This will check producer result asynchronously to avoid thread blocking
        future.addCallback(new ListenableFutureCallback<SendResult<String, CallStack>>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                log.error("Failed to send message", throwable);
            }

            @Override
            public void onSuccess(SendResult<String, CallStack> callStackSendResult) {

                log.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", callStackSendResult.getRecordMetadata().topic(),
                        callStackSendResult.getRecordMetadata().offset(),
                        callStackSendResult.getRecordMetadata().partition(),
                        callStackSendResult.getRecordMetadata().serializedValueSize()));
            }
        });
    }

    public ResponseEntity<ExecutionResponse> sendDwrLogs(SplunkPayLoad splunkPayLoad)
            throws ExecutionException, InterruptedException, TimeoutException {
        String topicName = "topic-dwr-call";
        String messageId = topicName+"-".concat(splunkPayLoad.getSid())+
                "-"+(Instant.now().toEpochMilli());
        ExecutionResponse resp = new ExecutionResponse();

        splunkPayLoad.setSid(messageId);

        SplunkResult result = splunkPayLoad.getResult();

//                Trigger the converter here :
//        result.setPerfLog(conversionService.convert(result.getRaw(), PerfLog.class));

//        log.info("===============================");
//        log.info("Converting PerfLog is \n{}",splunkPayLoad.getResult().getPerfLog());
//        log.info("===============================");
       /* try{
            splunkPayLoad.getResult().deserializePerfLog();
        }catch(IOException e){
            log.error("Could no deserialize PerfLog \t {}",e.getMessage());
        }*/

        ListenableFuture<SendResult<String, SplunkPayLoad>> future =
                 splunkPayLoadKafkaTemplate
                        .send(topicName,messageId,splunkPayLoad);

        future.get(1L,TimeUnit.SECONDS);

        future.addCallback(new ListenableFutureCallback<SendResult<String, SplunkPayLoad>>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                resp.status = "error";
                resp.msg = "failure while sending data to kafka. exception: " + throwable.getMessage();
                log.error(resp.msg);
            }

            @Override
            public void onSuccess(SendResult<String, SplunkPayLoad> splunkPayLoadSendResult) {
                resp.status = "ok";
                resp.msg = "message submitted successfully";

                log.info(String.format("\nProduced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", splunkPayLoadSendResult.getRecordMetadata().topic(),
                        splunkPayLoadSendResult.getRecordMetadata().offset(),
                        splunkPayLoadSendResult.getRecordMetadata().partition(),
                        splunkPayLoadSendResult.getRecordMetadata().serializedValueSize()));
            }

        });
        HttpStatus erespStatus = resp.status == "ok" ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<ExecutionResponse>(resp, erespStatus);
    }
}
