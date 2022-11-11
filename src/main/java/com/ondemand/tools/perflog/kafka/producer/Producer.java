package com.ondemand.tools.perflog.kafka.producer;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 10:39 AM
 * @description
 */


import com.ondemand.tools.perflog.models.CallStack;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public final class Producer {

    @Autowired
    private final KafkaTemplate<String, CallStack> kafkaTemplate;

    public Producer(KafkaTemplate<String, CallStack> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CallStack callStack) {
        String topicName = "perflog-for-dwr-calls";
        String messageId = topicName+"-"+(Instant.now().toEpochMilli() + "-").concat(UUID.randomUUID().toString());

        callStack.setId(messageId);
        ListenableFuture<SendResult<String, CallStack>> future = kafkaTemplate.send(topicName,messageId,callStack);

        //This will check producer result asynchronously to avoid thread blocking
        future.addCallback(new ListenableFutureCallback<SendResult<String, CallStack>>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                log.error("Failed to send message", throwable);
            }

            @Override
            public void onSuccess(SendResult<String, CallStack> stringStringSendResult) {

                log.info(String.format("Produced:\ntopic: %s\noffset: %d\npartition: %d\nvalue size: %d", stringStringSendResult.getRecordMetadata().topic(),
                        stringStringSendResult.getRecordMetadata().offset(),
                        stringStringSendResult.getRecordMetadata().partition(),
                        stringStringSendResult.getRecordMetadata().serializedValueSize()));
            }
        });
    }
}
