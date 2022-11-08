package com.ondemand.tools.perflog.kafka.producer;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 10:39 AM
 * @description
 */


import com.ondemand.tools.perflog.models.CallStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public final class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private final KafkaTemplate<String, CallStack> kafkaTemplate;

    public Producer(KafkaTemplate<String, CallStack> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CallStack callStack) {
        String topicName = "perflog-for-dwr-calls";
        ListenableFuture<SendResult<String, CallStack>> future = kafkaTemplate.send(topicName, callStack);

        //This will check producer result asynchronously to avoid thread blocking
        future.addCallback(new ListenableFutureCallback<SendResult<String, CallStack>>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                logger.error("Failed to send message", throwable);
            }

            @Override
            public void onSuccess(SendResult<String, CallStack> stringStringSendResult) {
                logger.info("Sent message successfully");
            }
        });
    }
}
