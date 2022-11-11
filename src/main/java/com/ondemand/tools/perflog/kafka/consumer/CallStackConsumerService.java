package com.ondemand.tools.perflog.kafka.consumer;

import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.repository.CallStackRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 2:13 PM
 * @description
 */
@Service
@AllArgsConstructor
@Slf4j
public class CallStackConsumerService {

    @Autowired
    private CallStackRepository callStackRepository ;

  /*  @KafkaListener(topics = "perflog-for-dwr-calls", containerFactory = "kafkaListenerContainerFactory")
    public void consume(CallStack stack) {
        callStackRepository.insert(stack);
        log.info("Consumed message :{}",stack.getN());
    }*/
    @KafkaListener(topics = "perflog-for-dwr-calls", containerFactory = "kafkaListenerContainerFactory")
    public void consume(final @Payload CallStack callStack,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s",
                ts, callStack, offset, key, partition, topic));
        log.info("Persisting message id: {}",callStack.getId());
        callStackRepository.insert(callStack);


    }

}
