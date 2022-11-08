package com.ondemand.tools.perflog.kafka.consumer;

import com.ondemand.tools.perflog.models.CallStack;
import com.ondemand.tools.perflog.repository.CallStackRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author Chandu D - i861116
 * @created 07/11/2022 - 2:13 PM
 * @description
 */
@Service
@AllArgsConstructor
public class CallStackConsumerService {

    @Autowired
    private CallStackRepository callStackRepository ;

    @KafkaListener(topics = "perflog-for-dwr-calls", containerFactory = "kafkaListenerContainerFactory")
    public void consume(CallStack stack) {
        callStackRepository.insert(stack);
    }

}
